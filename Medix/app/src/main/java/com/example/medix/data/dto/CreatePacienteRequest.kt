package com.example.medix.data.dto

import com.google.gson.annotations.SerializedName

data class CreatePacienteRequest(
    @SerializedName("tipo_documento")
    val tipoDocumento: String,
    @SerializedName("numero_documento")
    val numeroDocumento: String,
    val nombres: String,
    val apellidos: String,
    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String,
    val telefono: String?,
    val correo: String?,
    val estado: String,
    @SerializedName("id_usuario")
    val idUsuario: String?,
    @SerializedName("id_institucion")
    val idInstitucion: Long,
)
