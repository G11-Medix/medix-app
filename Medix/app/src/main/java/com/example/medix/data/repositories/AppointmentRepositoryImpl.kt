package com.example.medix.data.repositories


import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.AppointmentApi
import com.example.medix.data.mappers.toDomain
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.AppointmentRepository

class AppointmentRepositoryImpl(
    private val api: AppointmentApi
) : AppointmentRepository {

    override suspend fun getAppointments(): List<Appointment> {
        val idPaciente = SessionManager.getPacienteIdOrThrow().toInt()

        return api.getAppointments(idPaciente)
            .map { it.toDomain() }
    }
}