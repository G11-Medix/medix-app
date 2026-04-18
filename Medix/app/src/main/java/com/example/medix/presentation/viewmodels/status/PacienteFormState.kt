package com.example.medix.presentation.viewmodels.status

data class PacienteFormState(
    val tipoDocumento: String = "",
    val numeroDocumento: String = "",
    val nombres: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val telefono: String = "",
    val correo: String = "",
    val idEps: String = "",
    val epsNombre: String = ""

)