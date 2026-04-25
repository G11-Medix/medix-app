package com.example.medix.presentation.viewmodels.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.domain.repositories.VoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: VoiceRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var lastRequestText: String? = null

    init {
        bootstrapConversation()
    }

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(input = value) }
    }

    fun sendInputText() {
        val text = uiState.value.input.trim()
        if (text.isBlank() || uiState.value.isLoading) return

        _uiState.update { it.copy(input = "") }
        sendMessage(text = text, appendUserMessage = true)
    }

    fun sendOption(
        indexVisible: Int,
        optionId: String?,
        optionLabel: String,
        optionPayloadText: String?,
        enabled: Boolean,
    ) {
        if (!enabled || uiState.value.isLoading) return

        val normalizedPayload = normalizeForCompare(optionPayloadText ?: "")
        if (normalizedPayload == "mas fechas") {
            android.util.Log.d(
                "ChatViewModel",
                "Opción 'Mas fechas' detectada -> enviando text='mas fechas'"
            )
            sendMessage(
                text = "mas fechas",
                appendUserMessage = true,
                optionId = null,
                optionIndex = null,
            )
            return
        }

        val shouldUseOptionId = optionId == "cancel_appointment" || optionLabel.contains("cancel", ignoreCase = true)
        if (shouldUseOptionId) {
            android.util.Log.d(
                "ChatViewModel",
                "Cancelación detectada -> enviando solo text='3' sin option_id ni option_index"
            )
            sendMessage(
                text = indexVisible.toString(),
                appendUserMessage = true,
                optionId = null,
                optionIndex = null,
            )
            return
        }

        sendMessage(
            text = indexVisible.toString(),
            appendUserMessage = true,
            optionId = optionId,
            optionIndex = if (!optionId.isNullOrBlank()) indexVisible else null,
        )
    }

    fun retry() {
        if (uiState.value.isLoading) return
        val text = lastRequestText ?: ""
        val appendUser = text.isNotBlank()
        sendMessage(text = text, appendUserMessage = appendUser)
    }

    private fun bootstrapConversation() {
        viewModelScope.launch {
            val pacienteId = sessionManager.getPacienteId()
            if (pacienteId == null) {
                _uiState.update {
                    it.copy(errorMessage = "No se encontró sesión de paciente autenticado.")
                }
                return@launch
            }

            val sessionId = "chat-$pacienteId-${UUID.randomUUID()}"
            _uiState.update {
                it.copy(
                    pacienteId = pacienteId,
                    sessionId = sessionId,
                )
            }

            sendMessage(text = "", appendUserMessage = false)
        }
    }

    private fun sendMessage(
        text: String,
        appendUserMessage: Boolean,
        optionId: String? = null,
        optionIndex: Int? = null,
    ) {
        val sessionId = uiState.value.sessionId
        val pacienteId = uiState.value.pacienteId

        if (sessionId.isBlank() || pacienteId == null) {
            _uiState.update {
                it.copy(errorMessage = "No se pudo iniciar la conversación.")
            }
            return
        }

        if (appendUserMessage) {
            appendMessage(
                ChatMessage(
                    isUser = true,
                    text = text,
                )
            )
        }

        lastRequestText = text
        android.util.Log.d(
            "ChatViewModel",
            "Preparing chat request => session_id=$sessionId, text='$text', option_id=$optionId, option_index=$optionIndex, user_context.id_paciente=$pacienteId, user_context.is_authenticated=true"
        )
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                repository.sendChatConversationMessage(
                    text = text,
                    sessionId = sessionId,
                    pacienteId = pacienteId,
                    optionId = optionId,
                    optionIndex = optionIndex,
                )
            }.onSuccess { response ->
                val backendOptions = response.data?.options.orEmpty().map { option ->
                    ChatOptionUi(
                        id = option.id,
                        label = option.label,
                        enabled = option.enabled,
                    )
                }
                val extractedOptions = if (backendOptions.isEmpty()) {
                    extractOptionsFromAssistantText(response.response)
                } else {
                    emptyList()
                }
                val baseOptions = backendOptions.ifEmpty { extractedOptions }
                val options = appendMoreDatesOptionIfNeeded(baseOptions)

                val (assistantText, visibleOptions) = dedupeAssistantTextAndOptions(
                    assistantText = response.response,
                    options = options,
                    hasExplicitBackendOptions = backendOptions.isNotEmpty(),
                )

                if (assistantText.isNotBlank() || visibleOptions.isNotEmpty()) {
                    appendMessage(
                        ChatMessage(
                            isUser = false,
                            text = assistantText,
                            options = visibleOptions,
                        )
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error de red. Intenta nuevamente.",
                    )
                }
            }
        }
    }

    private fun appendMessage(message: ChatMessage) {
        _uiState.update { state ->
            state.copy(messages = state.messages + message)
        }
    }

    private fun dedupeAssistantTextAndOptions(
        assistantText: String,
        options: List<ChatOptionUi>,
        hasExplicitBackendOptions: Boolean,
    ): Pair<String, List<ChatOptionUi>> {
        if (!hasExplicitBackendOptions || assistantText.isBlank() || options.size < 2) {
            return assistantText to options
        }

        val normalizedAssistantText = normalizeForCompare(assistantText)
        val matchedOptions = options.count { option ->
            val normalizedLabel = normalizeForCompare(option.label)
            normalizedLabel.isNotBlank() && normalizedAssistantText.contains(normalizedLabel)
        }

        val hasEnumeratedFormat = assistantText.contains(Regex("""\b1[.)]\s+"""))
        if (matchedOptions < 2 || !hasEnumeratedFormat) {
            return assistantText to options
        }

        val cleanedText = stripEnumeratedOptionsFromText(assistantText, options)
        return cleanedText to options
    }

    private fun extractOptionsFromAssistantText(text: String): List<ChatOptionUi> {
        if (text.isBlank()) return emptyList()

        val multilineRegex = Regex("""(?m)^\s*(\d+)[.)-]\s+(.+?)\s*$""")
        val inlineRegex = Regex("""(\d+)[.)-]\s+(.+?)(?=(?:\s+\d+[.)-]\s+)|$)""")

        val multilineMatches = multilineRegex.findAll(text).toList()
        val matches = if (multilineMatches.size >= 2) {
            multilineMatches
        } else {
            inlineRegex.findAll(text).toList()
        }

        if (matches.size < 2) return emptyList()

        return matches
            .sortedBy { it.groupValues[1].toIntOrNull() ?: Int.MAX_VALUE }
            .map { match ->
                ChatOptionUi(
                    label = match.groupValues[2].trim(),
                    enabled = true,
                )
            }
            .distinctBy { normalizeForCompare(it.label) }
    }

    private fun appendMoreDatesOptionIfNeeded(options: List<ChatOptionUi>): List<ChatOptionUi> {
        if (options.isEmpty()) return options

        val alreadyHasMoreDates = options.any {
            normalizeForCompare(it.label) == "mas fechas" || normalizeForCompare(it.payloadText ?: "") == "mas fechas"
        }
        if (alreadyHasMoreDates) return options

        val dateRegex = Regex("""^\d{4}-\d{2}-\d{2}$""")
        val hasDateOptionsMenu = options.isNotEmpty() && options.all { option ->
            dateRegex.matches(option.label.trim())
        }

        if (!hasDateOptionsMenu) return options

        return options + ChatOptionUi(
            label = "Mas fechas",
            enabled = true,
            payloadText = "mas fechas",
            displayIndex = options.size + 1,
        )
    }

    private fun stripEnumeratedOptionsFromText(
        text: String,
        options: List<ChatOptionUi>,
    ): String {
        var cleanedText = text

        options.forEachIndexed { index, option ->
            val optionNumber = index + 1
            val escapedLabel = Regex.escape(option.label.trim())

            cleanedText = cleanedText.replace(
                Regex("""\b$optionNumber[.)]\s*$escapedLabel""", RegexOption.IGNORE_CASE),
                " "
            )
        }

        return cleanedText
            .replace(Regex("""\s{2,}"""), " ")
            .replace(Regex("""\s+([,.;:?!])"""), "$1")
            .trim()
    }

    private fun normalizeForCompare(value: String): String {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace(Regex("""\p{M}+"""), "")
            .lowercase()
            .replace(Regex("""\s+"""), " ")
            .trim()
    }
}

data class ChatUiState(
    val sessionId: String = "",
    val pacienteId: Long? = null,
    val input: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<ChatMessage> = emptyList(),
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val options: List<ChatOptionUi> = emptyList(),
)

data class ChatOptionUi(
    val id: String? = null,
    val label: String,
    val enabled: Boolean,
    val payloadText: String? = null,
    val displayIndex: Int? = null,
)














