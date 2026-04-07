package com.example.medix.data.dto

data class AuthEligibilityDto(
    val authorized: Boolean,
    val reason: String?,
    val paciente: AuthEligiblePacienteDto?,
)

data class AuthEligiblePacienteDto(
    val estado: String,
    val telefono: String?,
    val id_usuario: String?,
    val id_paciente: Long?,
)
