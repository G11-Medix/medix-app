package com.example.medix.data.dto

data class ConversationResponse(
    val response: String,
    val completed: Boolean,
    val action: String? = null,
    val data: ConversationData? = null,
    val audio_base64: String? = null,
    val audio_format: String? = null,
)

data class ConversationData(
    val step: String? = null,
    val options: List<ConversationOption>? = null,
)

data class ConversationOption(
    val id: String? = null,
    val label: String,
    val enabled: Boolean = true,
)
