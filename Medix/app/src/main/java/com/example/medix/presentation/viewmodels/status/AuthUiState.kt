package com.example.medix.presentation.viewmodels.status

data class AuthUiState(
    val phone: String = "",
    val otpCode: String = "",
    val otpSent: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val navigationTarget: AuthNavigationTarget = AuthNavigationTarget.NONE,
    val pacienteForm: PacienteFormState = PacienteFormState(),
)
