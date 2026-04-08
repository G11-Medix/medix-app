package com.example.medix.core.network

import com.example.medix.data.dto.AppointmentDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AppointmentApi {


    @GET("api/app/citas/{id_paciente}")
    suspend fun getAppointments(
        @Path("id_paciente") idPaciente: Int
    ): List<AppointmentDto>
}