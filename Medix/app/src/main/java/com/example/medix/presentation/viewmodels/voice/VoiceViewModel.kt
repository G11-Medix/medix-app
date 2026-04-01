package com.example.medix.presentation.viewmodels.voice

import android.content.Context
import android.media.AudioManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.domain.repositories.VoiceRepository
import com.example.medix.presentation.viewmodels.status.ConversationStatus
import com.example.medix.presentation.viewmodels.status.VoiceUiState
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder
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
    private val context: Context,
) : ViewModel() {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _isSpeakerOn = MutableStateFlow(false)
    val isSpeakerOn: StateFlow<Boolean> = _isSpeakerOn.asStateFlow()

    private var currentAudioFile: File? = null

    init {
        connectWebSocket()
    }

    // Recording

    fun startRecording() {
        runCatching {
            currentAudioFile = recorder.start()
            _uiState.update {
                it.copy(
                    status = ConversationStatus.LISTENING,
                    errorMessage = null,
                )
            }
        }.onFailure {
            showError("No se pudo iniciar la grabación: ${it.localizedMessage}")
        }
    }

    fun stopRecordingAndSend() {
        val audioFile = recorder.stop() ?: currentAudioFile

        if (audioFile == null) {
            showError("No se detectó audio para enviar.")
            return
        }

        _uiState.update {
            it.copy(
                status = ConversationStatus.PROCESSING,
                isLoading = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            runCatching {
                val text = repository.transcribeAudio(audioFile)
                _uiState.update { it.copy(userText = text) }

                repository.sendConversationMessage(text, _uiState.value.sessionId)
            }.onSuccess { response ->
                updateAssistantResponse(response.response, response.completed)
            }.onFailure {
                showError("Error procesando la solicitud: ${it.localizedMessage}")
            }
        }
    }


    // Text

    fun sendTextMessage(text: String) {
        if (text.isBlank()) return

        _uiState.update {
            it.copy(
                userText = text,
                status = ConversationStatus.PROCESSING,
                isLoading = true,
            )
        }

        viewModelScope.launch {
            runCatching {
                repository.sendConversationMessage(text, _uiState.value.sessionId)
            }.onSuccess { response ->
                updateAssistantResponse(response.response, response.completed)
            }.onFailure {
                showError("No se pudo enviar el texto: ${it.localizedMessage}")
            }
        }
    }

    // Websocket

    private fun connectWebSocket() {
        repository.connectWebSocket(
            sessionId = _uiState.value.sessionId,
            onMessage = { handleWebSocketPayload(it) },
            onStateChanged = { connected ->
                _uiState.update { it.copy(wsConnected = connected) }
            },
        )
    }

    private fun handleWebSocketPayload(payload: String) {
        runCatching {
            val json = JSONObject(payload)
            val response = json.optString("response")
            val state = json.optString("state")
            val completed = json.optBoolean("completed", false)

            val status = when (state.lowercase()) {
                "listening" -> ConversationStatus.LISTENING
                "processing" -> ConversationStatus.PROCESSING
                "responding" -> ConversationStatus.RESPONDING
                else -> _uiState.value.status
            }

            if (response.isNotBlank()) {
                updateAssistantResponse(response, completed)
            } else {
                _uiState.update { it.copy(status = status) }
            }
        }
    }

    fun sendTextByWebSocket(text: String) {
        if (text.isBlank()) return

        repository.sendWebSocketMessage(text, _uiState.value.sessionId)

        _uiState.update {
            it.copy(
                userText = text,
                status = ConversationStatus.PROCESSING,
                isLoading = true,
            )
        }
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
    }

    fun toggleSpeaker() {
        val newValue = !_isSpeakerOn.value
        _isSpeakerOn.value = newValue

        audioManager.isSpeakerphoneOn = newValue
    }

    // response

    private fun updateAssistantResponse(response: String, completed: Boolean) {
        _uiState.update {
            it.copy(
                assistantText = response,
                status = if (completed) ConversationStatus.IDLE else ConversationStatus.RESPONDING,
                isLoading = false,
                completed = completed,
                errorMessage = null,
            )
        }

        if (!_isMuted.value) {
            player.speak(response)
        }
    }

    // error

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = ConversationStatus.ERROR,
                isLoading = false,
                errorMessage = message,
            )
        }
    }

    override fun onCleared() {
        repository.closeWebSocket()
        player.release()
        super.onCleared()
    }
}