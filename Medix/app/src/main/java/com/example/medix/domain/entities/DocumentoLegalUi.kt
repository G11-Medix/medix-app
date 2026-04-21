package com.example.medix.domain.entities

data class DocumentoLegalUi(
    val id: Long,
    val version: String,
    val contenido: String,
    val fechaPublicacion: String
)