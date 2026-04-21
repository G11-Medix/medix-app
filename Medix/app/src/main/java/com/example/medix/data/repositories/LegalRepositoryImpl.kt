package com.example.medix.data.repositories

import com.example.medix.core.network.LegalApi
import com.example.medix.data.dto.AceptacionRequest
import com.example.medix.domain.entities.DocumentoLegalUi
import com.example.medix.domain.repositories.LegalRepository
import com.example.medix.data.mappers.toUi
import javax.inject.Inject


class LegalRepositoryImpl @Inject constructor(
    private val api: LegalApi
) : LegalRepository {

    override suspend fun getDocumento(): DocumentoLegalUi {
        return api.getDocumentoActivo().toUi()
    }

    override suspend fun aceptarDocumento(idDocumento: Long) {
        api.aceptarDocumento(
            AceptacionRequest(
                id_documento = idDocumento,
                dispositivo = "Android"
            )
        )
    }

    override suspend fun hasAcceptedLatest(): Boolean {
        return api.hasAcceptedLatest().accepted
    }
}