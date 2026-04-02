package com.example.medix.presentation.viewmodels.voice

import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.domain.repositories.VoiceRepository
import com.example.medix.presentation.viewmodels.status.ConversationStatus
import com.example.medix.presentation.viewmodels.status.VoiceUiState
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class VoiceViewModel(
    private val repository: VoiceRepository,
    private val recorder: AudioRecorder,
    private val player: AudioPlayer,
    context: Context,
) : ViewModel() {

    private val audioManager =
        context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _isSpeakerOn = MutableStateFlow(false)
    val isSpeakerOn: StateFlow<Boolean> = _isSpeakerOn.asStateFlow()

    private var currentAudioFile: File? = null
    private var isProcessingAudio = false
    private var isRecordingPressActive = false
    private var wsReadyForSession = false

    init {
        Log.d("VoiceViewModel", "Initializing VoiceViewModel with sessionId: ${_uiState.value.sessionId}")
        connectWebSocket()
    }

    fun startSchedulingSession(prompt: String) {
        Log.d("VoiceViewModel", "startSchedulingSession called with prompt: $prompt")
        Log.d("VoiceViewModel", "wsConnected: ${uiState.value.wsConnected}, wsReadyForSession: $wsReadyForSession")

        if (uiState.value.wsConnected && wsReadyForSession) {
            Log.i("VoiceViewModel", "Sending prompt via WebSocket")
            sendTextByWebSocket(prompt)
        } else {
            Log.i("VoiceViewModel", "WebSocket not ready, will retry in 500ms")
            viewModelScope.launch {
                delay(500)
                if (uiState.value.wsConnected && wsReadyForSession) {
                    Log.i("VoiceViewModel", "Retrying prompt via WebSocket")
                    sendTextByWebSocket(prompt)
                } else {
                    Log.i("VoiceViewModel", "Sending prompt via HTTP fallback")
                    sendTextMessage(prompt)
                }
            }
        }
    }

    // Recording

    fun startRecording() {
        if (isProcessingAudio || isRecordingPressActive) return

        runCatching {
            currentAudioFile = recorder.start()
            isRecordingPressActive = true
            _uiState.update {
                it.copy(
                    status = ConversationStatus.LISTENING,
                    errorMessage = null,
                )
            }
            Log.d("VoiceViewModel", "Recording started")
        }.onFailure {
            Log.e("VoiceViewModel", "Recording start failed", it)
            showError("No se pudo iniciar la grabación: ${it.localizedMessage}")
        }
    }

    fun stopRecordingAndSend() {
        if (!isRecordingPressActive || isProcessingAudio) return
        isRecordingPressActive = false

        val audioFile = recorder.stop() ?: currentAudioFile

        if (audioFile == null) {
            Log.w("VoiceViewModel", "No audio file to send")
            showError("No se detectó audio para enviar.")
            return
        }

        isProcessingAudio = true
        _uiState.update {
            it.copy(
                status = ConversationStatus.PROCESSING,
                isLoading = true,
                errorMessage = null,
            )
        }
        Log.d("VoiceViewModel", "Recording stopped, processing audio file: ${audioFile.name}")

        viewModelScope.launch {
            runCatching {
                Log.d("VoiceViewModel", "Transcribing audio...")
                val text = repository.transcribeAudio(audioFile)
                Log.i("VoiceViewModel", "Transcription result: $text")
                _uiState.update { it.copy(userText = text) }

                if (text.isBlank()) {
                    throw IllegalArgumentException("Transcription returned empty text")
                }

                val sentByWs = if (_uiState.value.wsConnected) {
                    repository.sendWebSocketMessage(text, _uiState.value.sessionId)
                } else {
                    false
                }

                if (sentByWs) {
                    Log.i("VoiceViewModel", "Audio transcription sent via WebSocket")
                    _uiState.update {
                        it.copy(
                            status = ConversationStatus.RESPONDING,
                            isLoading = true,
                        )
                    }
                    null
                } else {
                    Log.w("VoiceViewModel", "WebSocket unavailable, falling back to HTTP conversation")
                    repository.sendConversationMessage(text, _uiState.value.sessionId)
                }
            }.onSuccess { response ->
                if (response != null) {
                    Log.i("VoiceViewModel", "Conversation response received: ${response.response}")
                    updateAssistantResponse(response.response, response.completed)
                }
            }.onFailure {
                Log.e("VoiceViewModel", "Error processing audio request", it)
                showError("Error procesando la solicitud: ${it.localizedMessage}")
            }
            isProcessingAudio = false
        }
    }


    // Text

    fun sendTextMessage(text: String) {
        if (text.isBlank()) return

        Log.d("VoiceViewModel", "sendTextMessage: $text")
        _uiState.update {
            it.copy(
                userText = text,
                status = ConversationStatus.PROCESSING,
                isLoading = true,
            )
        }

        viewModelScope.launch {
            runCatching {
                Log.d("VoiceViewModel", "Sending message via HTTP")
                repository.sendConversationMessage(text, _uiState.value.sessionId)
            }.onSuccess { response ->
                Log.i("VoiceViewModel", "HTTP response received: ${response.response}")
                updateAssistantResponse(response.response, response.completed)
            }.onFailure {
                Log.e("VoiceViewModel", "HTTP request failed", it)
                showError("No se pudo enviar el texto: ${it.localizedMessage}")
            }
        }
    }

    // Websocket

    private fun connectWebSocket() {
        Log.d("VoiceViewModel", "Connecting WebSocket...")
        repository.connectWebSocket(
            sessionId = _uiState.value.sessionId,
            onMessage = {
                Log.d("VoiceViewModel", "WebSocket message received")
                handleWebSocketPayload(it)
            },
            onStateChanged = { connected ->
                Log.i("VoiceViewModel", "WebSocket state changed: connected=$connected")
                _uiState.update { it.copy(wsConnected = connected) }
                if (connected) {
                    wsReadyForSession = true
                    Log.i("VoiceViewModel", "WebSocket ready for session")
                } else {
                    wsReadyForSession = false
                }
            },
        )
    }

    private fun handleWebSocketPayload(payload: String) {
        runCatching {
            Log.d("VoiceViewModel", "Processing WebSocket payload: $payload")
            val json = JSONObject(payload)
            val response = json.optString("response")
            val state = json.optString("state")
            val completed = json.optBoolean("completed", false)

            Log.d("VoiceViewModel", "Parsed - response: '${response.take(50)}...', state: $state, completed: $completed")

            val status = when (state.lowercase()) {
                "listening" -> ConversationStatus.LISTENING
                "processing" -> ConversationStatus.PROCESSING
                "responding" -> ConversationStatus.RESPONDING
                else -> _uiState.value.status
            }

            if (response.isNotBlank()) {
                Log.i("VoiceViewModel", "Updating assistant response from WebSocket")
                updateAssistantResponse(response, completed)
            } else {
                Log.d("VoiceViewModel", "No response in payload, updating status only")
                _uiState.update {
                    it.copy(
                        status = status,
                        isLoading = status == ConversationStatus.PROCESSING || status == ConversationStatus.RESPONDING,
                    )
                }
            }
        }.onFailure {
            Log.e("VoiceViewModel", "Error parsing WebSocket payload", it)
        }
    }

    fun sendTextByWebSocket(text: String) {
        if (text.isBlank()) return

        Log.d("VoiceViewModel", "sendTextByWebSocket: $text, wsConnected: ${_uiState.value.wsConnected}")

        val sent = if (_uiState.value.wsConnected) {
            repository.sendWebSocketMessage(text, _uiState.value.sessionId)
        } else {
            false
        }

        if (!sent) {
            Log.w("VoiceViewModel", "WebSocket not connected/ready, falling back to HTTP")
            sendTextMessage(text)
            return
        }


        _uiState.update {
            it.copy(
                userText = text,
                status = ConversationStatus.PROCESSING,
                isLoading = true,
            )
        }
        Log.d("VoiceViewModel", "Message sent via WebSocket")
    }

    // Service audio recorder

    fun toggleMute() {
        val newValue = !_isMuted.value
        _isMuted.value = newValue

        if (newValue) {
            recorder.mute()
        } else {
            recorder.unmute()
        }
        Log.d("VoiceViewModel", "Mute toggled: $newValue")
    }

    fun toggleSpeaker() {
        val newValue = !_isSpeakerOn.value
        _isSpeakerOn.value = newValue
        Log.d("VoiceViewModel", "Speaker toggled: $newValue")
    }

    // response

    private fun updateAssistantResponse(response: String, completed: Boolean) {
        Log.i("VoiceViewModel", "Updating assistant response: '${response.take(50)}...', completed: $completed")

        _uiState.update {
            it.copy(
                assistantText = response,
                status = if (completed) ConversationStatus.IDLE else ConversationStatus.RESPONDING,
                isLoading = false,
                completed = completed,
                errorMessage = null,
            )
        }

        if (!_isMuted.value && response.isNotBlank()) {
            Log.d("VoiceViewModel", "Speaking response")
            player.speak(response)
        } else {
            Log.d("VoiceViewModel", "Not speaking: muted=${_isMuted.value}, blank=${response.isBlank()}")
        }
    }

    // error

    private fun showError(message: String) {
        Log.e("VoiceViewModel", "Error: $message")
        _uiState.update {
            it.copy(
                status = ConversationStatus.ERROR,
                isLoading = false,
                errorMessage = message,
            )
        }
    }

    override fun onCleared() {
        Log.d("VoiceViewModel", "onCleared called")
        repository.closeWebSocket()
        player.release()
        super.onCleared()
    }
}