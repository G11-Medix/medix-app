package com.example.medix.presentation.viewmodels.status

import com.example.medix.domain.entities.DocumentoLegalUi

data class ConsentUiState(
    val isLoading: Boolean = false,
    val document: DocumentoLegalUi? = null,
    val accepted: Boolean = false,
    val error: String? = null
)