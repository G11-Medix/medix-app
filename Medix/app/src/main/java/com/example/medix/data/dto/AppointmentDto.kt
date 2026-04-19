package com.example.medix.data.dto

data class AppointmentDto(
    val id: String,
    val nombre_ins: String,
    val especialidad: String,
    val fecha: String,
    val hora: String
)