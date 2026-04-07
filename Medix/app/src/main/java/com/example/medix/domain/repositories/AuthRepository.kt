package com.example.medix.domain.repositories

interface AuthRepository {

    suspend fun requestOtp(
        phone: String,
        createUser: Boolean = false,
    )

    suspend fun verifyOtp(
        phone: String,
        code: String,
    ): String

    fun currentAccessToken(): String?

    fun currentUserId(): String?
}
