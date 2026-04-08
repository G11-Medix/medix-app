package com.example.medix.core.network

import com.example.medix.data.dto.UserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {

    @GET("app/profile/{id_paciente}")
    suspend fun getProfile(
        @Path("id_paciente") idPaciente: Int
    ): UserProfileDto
}