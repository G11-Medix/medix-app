package com.example.medix.data.dto


data class AppointmentDto(
    val id: Long,
    val id_institucion: Long,
    val nombre_ins: String,
    val logo_url: String,
    val especialidad: String,
    val estado: String,
    val fecha: String,
    val hora: String
)

