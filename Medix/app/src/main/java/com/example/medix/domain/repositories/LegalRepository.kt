package com.example.medix.domain.repositories

import com.example.medix.domain.entities.DocumentoLegalUi

interface LegalRepository {
    suspend fun getDocumento(): DocumentoLegalUi
    suspend fun aceptarDocumento(idDocumento: Long)

    suspend fun hasAcceptedLatest(): Boolean
}