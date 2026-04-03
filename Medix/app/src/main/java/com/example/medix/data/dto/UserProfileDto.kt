package com.example.medix.data.dto

data class UserProfileDto(
    val nombres: String,
    val apellidos: String,
    val documento: String,
    val eps: String,
    val correo: String,
    val telefono: String,
    val correoVerificado: Boolean,
    val telefonoVerificado: Boolean
)