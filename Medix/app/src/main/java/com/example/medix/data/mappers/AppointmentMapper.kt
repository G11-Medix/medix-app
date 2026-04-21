package com.example.medix.data.mappers

import com.example.medix.data.dto.AppointmentDto
import com.example.medix.domain.entities.Appointment

fun AppointmentDto.toDomain(): Appointment {
    return Appointment(
        id = id,
        id_institucion = id_institucion,
        name = nombre_ins,
        logo_url= logo_url,
        specialty = especialidad,
        state = estado,
        date = fecha,
        hour = hora
    )
}