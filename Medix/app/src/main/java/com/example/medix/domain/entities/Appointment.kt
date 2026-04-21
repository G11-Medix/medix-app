package com.example.medix.domain.entities

data class Appointment(
    val id: Long,
    val id_institucion: Long,
    val name: String,
    val logo_url: String,
    val specialty: String,
    val state: String,
    val date: String,
    val hour: String
)



