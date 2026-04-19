package com.example.medix.data.mappers

import com.example.medix.data.dto.AppointmentDto
import com.example.medix.domain.entities.Appointment

fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        id = id,
        name = nombre_ins,
        specialty = especialidad,
        date = fecha,
        hour = hora
    )
}