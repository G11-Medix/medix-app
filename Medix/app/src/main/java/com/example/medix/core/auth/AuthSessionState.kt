package com.example.medix.core.auth

data class AuthSessionState(
    val token: String? = null,
    val refreshToken: String? = null,
    val pacienteId: Long? = null
) {
    val isLoggedIn: Boolean
        get() = !token.isNullOrBlank()
}