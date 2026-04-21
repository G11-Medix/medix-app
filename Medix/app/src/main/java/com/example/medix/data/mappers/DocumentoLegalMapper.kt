package com.example.medix.data.mappers

import com.example.medix.data.dto.DocumentoLegalDto
import com.example.medix.domain.entities.DocumentoLegalUi

fun DocumentoLegalDto.toUi() = DocumentoLegalUi(
    id = id_documento,
    version = version,
    contenido = contenido,
    fechaPublicacion = fecha_publicacion
)