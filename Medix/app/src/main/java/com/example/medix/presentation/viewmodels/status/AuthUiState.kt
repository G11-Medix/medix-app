package com.example.medix.presentation.viewmodels.status

import com.example.medix.data.dto.EpsDto

data class AuthUiState(
    val phone: String = "",
    val phoneCountryCode: String = "+57",
    val otpCode: String = "",
    val otpSent: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingEps: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val navigationTarget: AuthNavigationTarget = AuthNavigationTarget.NONE,
    val pacienteForm: PacienteFormState = PacienteFormState(),
    val epsOptions: List<EpsDto> = emptyList(),
)
