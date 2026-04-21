package com.example.medix.data.dto

data class ConversationResponse(
    val response: String,
    val completed: Boolean,
    val audio_base64: String? = null,
    val audio_format: String? = null,
)