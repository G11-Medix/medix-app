package com.example.medix.domain.repositories

import com.example.medix.data.dto.AuthEligibilityDto
import com.example.medix.data.dto.CreatePacienteRequest
import com.example.medix.data.dto.PacienteDto

interface PacienteRepository {

    suspend fun getAuthEligibilityByTelefono(telefono: String): AuthEligibilityDto?

    suspend fun createPaciente(request: CreatePacienteRequest): PacienteDto
}
