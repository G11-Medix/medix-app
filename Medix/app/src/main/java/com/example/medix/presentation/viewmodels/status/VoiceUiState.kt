package com.example.medix.presentation.viewmodels.status

import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.presentation.viewmodels.status.ConversationStatus.*
import java.util.UUID

data class VoiceUiState(
    val sessionId: String = UUID.randomUUID().toString(),
    val userText: String = "",
    val assistantText: String = "",
    val status: ConversationStatus = IDLE,
    val isLoading: Boolean = false,
    val completed: Boolean = false,
    val wsConnected: Boolean = false,
    val errorMessage: String? = null,
    val appointmentConfirmation: AppointmentConfirmationDto? = null
)