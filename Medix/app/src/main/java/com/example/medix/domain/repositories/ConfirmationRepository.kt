package com.example.medix.domain.repositories

import com.example.medix.data.dto.AppointmentConfirmationDto

interface ConfirmationRepository {
    suspend fun getConfirmation(): AppointmentConfirmationDto
}