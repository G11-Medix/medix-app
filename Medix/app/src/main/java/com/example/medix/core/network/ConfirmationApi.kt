package com.example.medix.core.network

import com.example.medix.data.dto.AppointmentConfirmationDto
import retrofit2.http.GET

interface ConfirmationApi {

    @GET("appointment/confirmation")
    suspend fun getConfirmation(): AppointmentConfirmationDto
}