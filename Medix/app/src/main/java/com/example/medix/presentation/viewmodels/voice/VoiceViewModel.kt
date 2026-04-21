package com.example.medix.presentation.viewmodels.voice

import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.domain.repositories.VoiceRepository
import com.example.medix.presentation.viewmodels.status.ConversationStatus
import com.example.medix.presentation.viewmodels.status.VoiceUiState
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val repository: VoiceRepository,
    private val recorder: AudioRecorder,
    private val sessionManager: SessionManager,
    private val player: AudioPlayer,

    @ApplicationContext context: Context
) : ViewModel() {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()


    private var currentAudioFile: File? = null
    private var isProcessingAudio = false
    private var isRecordingPressActive = false
    private var wsReadyForSession = false

    /*
    init {
        connectWebSocket()
    }
    */
    init {
        viewModelScope.launch {
            sessionManager.sessionFlow
                .map { it.pacienteId }
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest {
                    connectWebSocket()
                }
        }
    }

    fun startSchedulingSession(prompt: String) {
        if (uiState.value.wsConnected && wsReadyForSession) {
            sendTextByWebSocket(prompt)
        } else {
            viewModelScope.launch {
                delay(500)
                if (uiState.value.wsConnected && wsReadyForSession) {
                    sendTextByWebSocket(prompt)
                } else {
                    sendTextMessage(prompt)
                }
            }
        }
    }

    // RECORDING

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
        }.onFailure {
            showError("No se pudo iniciar la grabación")
        }
    }

    fun stopRecordingAndSend() {
        if (!isRecordingPressActive || isProcessingAudio) return

        isRecordingPressActive = false
        val audioFile = recorder.stop() ?: currentAudioFile

        if (audioFile == null) {
            showError("No se detectó audio")
            return
        }

        isProcessingAudio = true

        _uiState.update {
            it.copy(
                status = ConversationStatus.PROCESSING,
                isLoading = true
            )
        }

        viewModelScope.launch {
            runCatching {
                val text = repository.transcribeAudio(audioFile)

                if (text.isBlank()) error("Empty transcription")

                setUserText(text)

                val sentByWs = if (_uiState.value.wsConnected) {
                    repository.sendWebSocketMessage(text, _uiState.value.sessionId)
                } else false

                if (sentByWs) {
                    _uiState.update {
                        it.copy(
                            status = ConversationStatus.RESPONDING,
                            isLoading = true
                        )
                    }
                    null
                } else {
                    repository.sendConversationMessage(text, _uiState.value.sessionId)
                }
            }.onSuccess { response ->
                response?.let {
                    updateAssistantResponse(it.response, it.completed, it.audio_base64)
                }
            }.onFailure {
                showError("Error procesando audio")
            }

            isProcessingAudio = false
        }
    }

    private fun setUserText(text: String) {
        _uiState.update { it.copy(userText = text) }
    }

    // TEXT

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
            }.onSuccess {
                updateAssistantResponse(it.response, it.completed, it.audio_base64)
            }.onFailure {
                showError("Error enviando texto")
            }
        }
    }

    // WEBSOCKET

    private fun connectWebSocket() {
        viewModelScope.launch {
            repository.connectWebSocket(
                sessionId = _uiState.value.sessionId,
                onMessage = { handleWebSocketPayload(it) },
                onStateChanged = { connected ->
                    _uiState.update { it.copy(wsConnected = connected) }
                    wsReadyForSession = connected
                }
            )
        }
    }

    private fun handleWebSocketPayload(payload: String) {
        runCatching {
            val json = JSONObject(payload)

            val response = json.optString("response")
            val state = json.optString("state")
            val completed = json.optBoolean("completed", false)
            val action = json.optString("action", null)
            val data = json.optJSONObject("data")
            val audioBase64 = json.optString("audio_base64").takeIf { it.isNotEmpty() }

            val status = when (state.lowercase()) {
                "listening" -> ConversationStatus.LISTENING
                "processing" -> ConversationStatus.PROCESSING
                "responding" -> ConversationStatus.RESPONDING
                else -> _uiState.value.status
            }

            // ✅ 1. Procesa acción SIEMPRE (independiente del response)
            if (completed && action == "CREATE_APPOINTMENT" && data != null) {
                val appointment = mapToAppointmentDto(data)

                appointment?.let {
                    _uiState.update { state ->
                        state.copy(appointmentConfirmation = it)
                    }
                }
            }

            // ✅ 2. Manejo de respuesta
            if (response.isNotBlank()) {
                updateAssistantResponse(response, completed, audioBase64)
            } else {
                _uiState.update {
                    it.copy(
                        status = status,
                        isLoading = status != ConversationStatus.IDLE
                    )
                }
            }

        }.onFailure {
            showError("Error procesando mensaje WS")
        }
    }

    private fun mapToAppointmentDto(data: JSONObject): AppointmentConfirmationDto? {
        val confirmation = data.optJSONObject("confirmation") ?: return null

        return AppointmentConfirmationDto(
            doctorName = confirmation.optString("doctor"),
            date = confirmation.optString("fecha"),
            clinicName = confirmation.optString("institucion"),
            address = confirmation.optString("direccion"),
            lat = confirmation.optDouble("latitud"),
            lon = confirmation.optDouble("longitud"),
            status = confirmation.optString("estado").uppercase(),

            title = "Cita agendada",
            message = "Tu cita ha sido programada correctamente"
        )
    }

    fun sendTextByWebSocket(text: String) {
        if (text.isBlank()) return

        val sent = if (_uiState.value.wsConnected) {
            repository.sendWebSocketMessage(text, _uiState.value.sessionId)
        } else false

        if (!sent) {
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
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
    }

    private fun updateAssistantResponse(
        response: String,
        completed: Boolean,
        audioBase64: String? = null,
    ) {
        _uiState.update {
            it.copy(
                assistantText = response,
                status = if (completed) ConversationStatus.IDLE else ConversationStatus.RESPONDING,
                isLoading = false,
                completed = completed
            )
        }

        if (response.isNotBlank()) {
            if (!audioBase64.isNullOrEmpty()) {
                player.playBase64Audio(audioBase64, isMuted = _isMuted.value, fallbackText = response)
            } else {
                player.speak(response, isMuted = _isMuted.value)
            }
        }
    }

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