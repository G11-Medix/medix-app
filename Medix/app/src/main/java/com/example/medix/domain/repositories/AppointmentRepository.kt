package com.example.medix.domain.repositories

import com.example.medix.domain.entities.Appointment

interface AppointmentRepository {
    suspend fun getAppointments(): List<Appointment>
}