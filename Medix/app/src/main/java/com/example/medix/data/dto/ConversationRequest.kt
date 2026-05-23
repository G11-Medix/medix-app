package com.example.medix.data.dto

data class ConversationRequest(
    val text: String,
    val session_id: String,
    val user_context: ConversationUserContext? = null,
    val option_id: String? = null,
    val option_index: Int? = null,
)

data class ConversationUserContext(
    val id_paciente: Long,
    val is_authenticated: Boolean,
)
