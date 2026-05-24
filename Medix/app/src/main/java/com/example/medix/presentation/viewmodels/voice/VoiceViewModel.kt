package com.example.medix.presentation.viewmodels.voice

import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.data.dto.ConversationData
import com.example.medix.domain.repositories.VoiceRepository
import com.example.medix.presentation.viewmodels.status.ConversationStatus
import com.example.medix.presentation.viewmodels.status.VoiceUiState
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.ArrayDeque
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
    private val pendingMessages = ArrayDeque<String>()

    private val appointmentCompletionActions = setOf(
        "CREATE_APPOINTMENT",
        "RESCHEDULE_APPOINTMENT",
        "REPROGRAM_APPOINTMENT",
    )

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
        queueOrSendMessage(prompt)
    }

    // RECORDING

    fun startRecording() {
        if (isProcessingAudio || isRecordingPressActive) return

        runCatching {
            // If assistant is speaking, interrupt playback before starting to record
            try { player.stopPlayback() } catch (_: Exception) {}

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
                sendTextByWebSocket(text)
            }.onSuccess {
            }.onFailure {
                showError("Error procesando audio")
            }

            isProcessingAudio = false
        }
    }

    // TEXT

    fun sendTextMessage(text: String) {
        queueOrSendMessage(text)
    }

    // WEBSOCKET

    private fun connectWebSocket() {
        viewModelScope.launch {
            repository.connectWebSocket(
                sessionId = _uiState.value.sessionId,
                onMessage = { handleWebSocketPayload(it) },
                onStateChanged = { connected ->
                    _uiState.update { it.copy(wsConnected = connected) }
                    if (!connected) {
                        wsReadyForSession = false
                    }
                }
            )
        }
    }

    private fun handleWebSocketPayload(payload: String) {
        runCatching {
            val json = JSONObject(payload)
            val type = json.optString("type")
            if (type.equals("init_ack", ignoreCase = true)) {
                val authenticated = json.optBoolean("authenticated", false)
                handleInitAck(authenticated)
                return
            }

            val response = json.optString("response")
            val state = json.optString("state")
            val completed = json.optBoolean("completed", false)
            val action = json.optString("action").takeIf { it.isNotBlank() }
            val data = json.optJSONObject("data")
            val audioBase64 = json.optString("audio_base64").takeIf { it.isNotEmpty() }

            val status = when (state.lowercase()) {
                "listening" -> ConversationStatus.LISTENING
                "processing" -> ConversationStatus.PROCESSING
                "responding" -> ConversationStatus.RESPONDING
                else -> _uiState.value.status
            }

            if (completed && isAppointmentCompletionAction(action) && data != null) {
                val appointment = mapToAppointmentDto(
                    data = data,
                    action = action!!,
                    responseText = response,
                )

                appointment?.let {
                    Log.d("AppointmentConfirmation", "Appointment recibido: $it")
                    _uiState.update { state ->
                        state.copy(
                            appointmentConfirmation = it,
                            navigateToConfirmation = true,
                        )
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

    private fun handleAppointmentCompletion(
        completed: Boolean,
        action: String?,
        responseText: String,
        data: ConversationData?,
    ) {
        if (!completed || !isAppointmentCompletionAction(action) || data == null) return

        val appointment = mapToAppointmentDto(
            data = data,
            action = action!!,
            responseText = responseText,
        ) ?: return

        _uiState.update {
            it.copy(
                appointmentConfirmation = appointment,
                navigateToConfirmation = true,
            )
        }
    }

    private fun isAppointmentCompletionAction(action: String?): Boolean {
        val normalized = action?.uppercase() ?: return false
        return normalized in appointmentCompletionActions
    }

    private fun mapToAppointmentDto(
        data: ConversationData,
        action: String,
        responseText: String,
    ): AppointmentConfirmationDto? {
        val confirmation = data.confirmation ?: return null

        return AppointmentConfirmationDto(
            appointmentId = data.appointment_id,
            doctorName = confirmation.doctor.orEmpty(),
            date = confirmation.fecha.orEmpty(),
            clinicName = confirmation.institucion.orEmpty(),
            address = confirmation.direccion.orEmpty(),
            lat = confirmation.latitud ?: 0.0,
            lon = confirmation.longitud ?: 0.0,
            title = mapTitleForAction(action),
            message = responseText,
            responseText = responseText,
            status = mapBackendStatus(confirmation.estado),
        )
    }

    private fun mapToAppointmentDto(
        data: JSONObject,
        action: String,
        responseText: String,
    ): AppointmentConfirmationDto? {
        val confirmation = data.optJSONObject("confirmation") ?: return null

        return AppointmentConfirmationDto(
            appointmentId = data.optString("appointment_id").takeIf { it.isNotBlank() },
            doctorName = confirmation.optString("doctor"),
            date = confirmation.optString("fecha"),
            clinicName = confirmation.optString("institucion"),
            address = confirmation.optString("direccion"),
            lat = confirmation.optDouble("latitud"),
            lon = confirmation.optDouble("longitud"),
            title = mapTitleForAction(action),
            message = responseText,
            responseText = responseText,
            status = mapBackendStatus(confirmation.optString("estado")),
        )
    }

    private fun mapTitleForAction(action: String): String {
        return when (action.uppercase()) {
            "RESCHEDULE_APPOINTMENT", "REPROGRAM_APPOINTMENT" -> "Cita reprogramada"
            else -> "Cita agendada"
        }
    }

    private fun mapBackendStatus(rawStatus: String?): String {
        return when (rawStatus?.trim()?.lowercase()) {
            "pending" -> "PENDING"
            "cancelled", "canceled" -> "CANCELLED"
            else -> "SUCCESS"
        }
    }

    fun onConfirmationNavigationHandled() {
        _uiState.update { it.copy(navigateToConfirmation = false) }
    }

    fun sendTextByWebSocket(text: String) {
        queueOrSendMessage(text)
    }

    private fun queueOrSendMessage(text: String) {
        if (text.isBlank()) return

        if (!canSendMessages()) {
            pendingMessages.addLast(text)
            _uiState.update {
                it.copy(
                    userText = text,
                    status = ConversationStatus.PROCESSING,
                    isLoading = true,
                )
            }
            return
        }

        val sent = repository.sendWebSocketMessage(text, _uiState.value.sessionId)
        if (!sent) {
            pendingMessages.addLast(text)
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

    private fun handleInitAck(authenticated: Boolean) {
        wsReadyForSession = authenticated
        if (!authenticated) {
            showError("No se pudo autenticar la sesion por WebSocket")
            return
        }
        flushPendingMessages()
    }

    private fun flushPendingMessages() {
        while (pendingMessages.isNotEmpty() && canSendMessages()) {
            val next = pendingMessages.removeFirst()
            val sent = repository.sendWebSocketMessage(next, _uiState.value.sessionId)
            if (!sent) {
                pendingMessages.addFirst(next)
                return
            }
            _uiState.update {
                it.copy(
                    userText = next,
                    status = ConversationStatus.PROCESSING,
                    isLoading = true,
                )
            }
        }
    }

    private fun canSendMessages(): Boolean {
        return _uiState.value.wsConnected && wsReadyForSession
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
            val onAudioDone = {
                // Return to IDLE or LISTENING state after audio finishes
                _uiState.update { state ->
                    if (state.status == ConversationStatus.RESPONDING) {
                        state.copy(status = ConversationStatus.IDLE)
                    } else state
                }
            }

            if (!audioBase64.isNullOrEmpty()) {
                player.playBase64Audio(
                    audioBase64,
                    isMuted = _isMuted.value,
                    fallbackText = response,
                    onDone = onAudioDone
                )
            } else {
                player.speak(
                    response,
                    isMuted = _isMuted.value,
                    onDone = onAudioDone
                )
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