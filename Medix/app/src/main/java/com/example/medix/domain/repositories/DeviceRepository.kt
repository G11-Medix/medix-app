package com.example.medix.domain.repositories

interface DeviceRepository {
    suspend fun sendFcmToken(token: String)
}