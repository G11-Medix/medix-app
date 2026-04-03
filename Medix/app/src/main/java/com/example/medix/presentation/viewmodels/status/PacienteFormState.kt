package com.example.medix.presentation.viewmodels.status

data class PacienteFormState(
    val tipoDocumento: String = "",
    val numeroDocumento: String = "",
    val nombres: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val telefono: String = "",
    val correo: String = "",
    val estado: String = "PENDIENTE",
    val idInstitucion: String = "",
)
