package com.example.medix.core.network

import com.example.medix.data.dto.AppointmentDto
import retrofit2.http.POST
import retrofit2.http.Body

interface DeviceApi {

    @POST("api/dispositivos/token")
    suspend fun saveToken(
        @Body body: Map<String, String>
    )
}