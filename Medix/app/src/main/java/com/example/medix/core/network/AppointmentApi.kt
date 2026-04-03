package com.example.medix.core.network

import com.example.medix.data.dto.AppointmentDto
import retrofit2.http.GET

interface AppointmentApi {

    @GET("appointments")
    suspend fun getAppointments(): List<AppointmentDto>
}