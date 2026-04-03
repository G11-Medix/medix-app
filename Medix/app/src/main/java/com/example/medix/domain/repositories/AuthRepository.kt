package com.example.medix.domain.repositories

interface AuthRepository {

    suspend fun requestOtp(phone: String)

    suspend fun verifyOtp(
        phone: String,
        code: String,
    ): String

    fun currentUserId(): String?
}
