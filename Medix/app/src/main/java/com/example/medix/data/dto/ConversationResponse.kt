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
    val appointment_id: String? = null,
    val confirmation: ConversationConfirmation? = null,
)

data class ConversationConfirmation(
    val doctor: String? = null,
    val fecha: String? = null,
    val institucion: String? = null,
    val direccion: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val estado: String? = null,
)

data class ConversationOption(
    val id: String? = null,
    val label: String,
    val enabled: Boolean = true,
)
