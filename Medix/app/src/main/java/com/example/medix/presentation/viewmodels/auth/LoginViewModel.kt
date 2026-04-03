package com.example.medix.presentation.viewmodels.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.AuthApi
import com.example.medix.data.dto.LoginRequest

import kotlinx.coroutines.launch

class LoginViewModel(
    private val api: AuthApi
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null

            try {
                val response = api.login(
                    LoginRequest(email, password)
                )

                SessionManager.token = response.access_token

                onSuccess()

            } catch (e: Exception) {
                error = "Credenciales incorrectas"
            } finally {
                isLoading = false
            }
        }
    }
}