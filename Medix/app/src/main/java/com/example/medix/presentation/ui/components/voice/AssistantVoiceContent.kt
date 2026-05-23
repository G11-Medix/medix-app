package com.example.medix.presentation.ui.components.voice

sealed class AssistantVoiceContent {
    data class PlainText(val text: String) : AssistantVoiceContent()

    data class NumberedOptions(
        val title: String,
        val options: List<OptionItem>,
        val footer: String? = null,
    ) : AssistantVoiceContent()
}

data class OptionItem(
    val number: Int,
    val label: String,
)

