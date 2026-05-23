package com.example.medix.data.repositories

import com.example.medix.core.network.ConfirmationApi
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.domain.repositories.ConfirmationRepository

class ConfirmationRepositoryImpl(
    private val api: ConfirmationApi
) : ConfirmationRepository {

    override suspend fun getConfirmation(): AppointmentConfirmationDto {
        return api.getConfirmation()
    }
}