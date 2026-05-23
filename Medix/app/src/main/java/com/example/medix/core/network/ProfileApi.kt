package com.example.medix.core.network

import com.example.medix.data.dto.UserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {


    @GET("api/pacientes/{id_paciente}/profile")
    suspend fun getProfile(
        @Path("id_paciente") idPaciente: Int
    ): UserProfileDto
}