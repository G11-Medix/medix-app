package com.example.medix.data.repositories

import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.DeviceApi
import com.example.medix.data.sources.local.TokenDataStore
import com.example.medix.domain.repositories.DeviceRepository
import javax.inject.Inject
import javax.inject.Singleton

class DeviceRepositoryImpl @Inject constructor(
    private val api: DeviceApi,
    private val sessionManager: SessionManager,
    private val tokenStorage: TokenDataStore
) : DeviceRepository {

    override suspend fun sendFcmToken(token: String) {

        tokenStorage.saveToken(token)

        api.saveToken(
            mapOf(
                "token_dispositivo" to token,
                "plataforma" to "android"
            )
        )
    }
}