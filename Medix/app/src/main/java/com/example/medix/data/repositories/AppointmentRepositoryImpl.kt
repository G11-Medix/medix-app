package com.example.medix.data.repositories

import android.util.Log
import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.AppointmentApi
import com.example.medix.data.mappers.toDomain
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.AppointmentRepository
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val api: AppointmentApi,
    private val sessionManager: SessionManager
) : AppointmentRepository {

    override suspend fun getAppointments(): List<Appointment> {
        val idPaciente = sessionManager.requirePacienteId().toInt()

        val response = api.getAppointments(idPaciente)

        Log.d("AppointmentsRepo", "📥 Respuesta cruda API: $response")

        val mapped = response.map { it.toDomain() }

        Log.d("AppointmentsRepo", "✅ Resultado mapeado: $mapped")

        return mapped
    }
}