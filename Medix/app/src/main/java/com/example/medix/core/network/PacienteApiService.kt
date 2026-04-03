package com.example.medix.core.network

import com.example.medix.data.dto.AuthEligibilityDto
import com.example.medix.data.dto.CreatePacienteRequest
import com.example.medix.data.dto.PacienteDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PacienteApiService {

    @GET("auth/eligibility/{telefono}")
    suspend fun getAuthEligibilityByTelefono(
        @Path("telefono") telefono: String,
    ): Response<AuthEligibilityDto>

    @POST("pacientes")
    suspend fun createPaciente(
        @Body request: CreatePacienteRequest,
    ): PacienteDto
}
