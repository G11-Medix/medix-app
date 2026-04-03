package com.example.medix.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medix.domain.repositories.AuthRepository
import com.example.medix.domain.repositories.PacienteRepository

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val pacienteRepository: PacienteRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                authRepository = authRepository,
                pacienteRepository = pacienteRepository,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
