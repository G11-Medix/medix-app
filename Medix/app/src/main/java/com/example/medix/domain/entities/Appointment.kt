package com.example.medix.domain.entities

data class Appointment(
    val id: String,
    val name: String,
    val specialty: String,
    val date: String
)