package com.example.medix.data.repositories

import android.util.Log
import com.example.medix.core.network.PacienteApiService
import com.example.medix.data.dto.AuthEligibilityDto
import com.example.medix.data.dto.CreatePacienteRequest
import com.example.medix.data.dto.EpsDto
import com.example.medix.data.dto.PacienteDto
import com.example.medix.domain.repositories.PacienteRepository
import retrofit2.HttpException

class PacienteRepositoryImpl(
    private val apiService: PacienteApiService,
) : PacienteRepository {
    override suspend fun getEps(): List<EpsDto> {
        return apiService.getEps()
    }

    override suspend fun getAuthEligibilityByTelefono(telefono: String): AuthEligibilityDto? {
        val response = apiService.getAuthEligibilityByTelefono(telefono)

        return when {
            response.isSuccessful -> response.body()
            response.code() == 404 -> null
            else -> throw HttpException(response)
        }
    }

    override suspend fun createPaciente(request: CreatePacienteRequest): PacienteDto {

        Log.d("PacienteRepo", "CREATE PACIENTE REQUEST => $request")

        return try {
            val response = apiService.createPaciente(request)

            Log.d("PacienteRepo", "CREATE PACIENTE RESPONSE => $response")

            response
        } catch (e: Exception) {
            Log.e("PacienteRepo", "CREATE PACIENTE ERROR", e)
            throw e
        }
    }
}
