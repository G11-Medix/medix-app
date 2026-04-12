package com.example.medix.data.repositories

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

        return api.getAppointments(idPaciente)
            .map { it.toDomain() }
    }
}