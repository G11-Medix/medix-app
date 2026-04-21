package com.example.medix.data.dto

data class DocumentoLegalDto(
    val id_documento: Long,
    val version: String,
    val contenido: String,
    val fecha_publicacion: String
)

data class AceptacionRequest(
    val id_documento: Long,
    val dispositivo: String
)

data class ConsentStatusDto(
    val accepted: Boolean
)
