package com.example.medix.core.auth

data class AuthSessionState(
    val token: String? = null,
) {
    val isLoggedIn: Boolean
        get() = !token.isNullOrBlank()
}
