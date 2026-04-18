package com.example.medix.core.network

import com.example.medix.data.dto.AceptacionRequest
import com.example.medix.data.dto.ConsentStatusDto
import com.example.medix.data.dto.DocumentoLegalDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface LegalApi {


    @GET("api/aceptacion-documento/activo")
    suspend fun getDocumentoActivo(): DocumentoLegalDto


    @POST("api/aceptacion-documento")
    suspend fun aceptarDocumento(
        @Body request: AceptacionRequest
    )


    @GET("api/aceptacion-documento/estado")
    suspend fun hasAcceptedLatest(): ConsentStatusDto
}