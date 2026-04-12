package com.example.medix.data.repositories

import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.ProfileApi
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val sessionManager: SessionManager
) : ProfileRepository {

    override suspend fun getProfile(): UserProfileDto {
        val idPaciente = sessionManager.requirePacienteId().toInt()
        return api.getProfile(idPaciente)
    }
}