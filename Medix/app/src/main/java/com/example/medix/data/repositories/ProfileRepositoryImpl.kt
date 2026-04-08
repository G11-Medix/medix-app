package com.example.medix.data.repositories

import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.ProfileApi
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.domain.repositories.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): UserProfileDto {
        val idPaciente = SessionManager.getPacienteIdOrThrow().toInt()

        return api.getProfile(idPaciente)
    }
}