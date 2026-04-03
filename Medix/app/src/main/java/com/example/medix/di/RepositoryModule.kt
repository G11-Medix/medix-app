package com.example.medix.di

import com.example.medix.core.network.WebSocketClient
import com.example.medix.data.repositories.AppointmentRepositoryImpl
import com.example.medix.data.repositories.VoiceRepositoryImpl
import com.example.medix.domain.repositories.AppointmentRepository
import com.example.medix.domain.repositories.VoiceRepository

object RepositoryModule {

    fun provideAppointmentRepository(): AppointmentRepository {
        return AppointmentRepositoryImpl(
            api = ApiModule.provideAppointmentApi()
        )
    }

    fun provideVoiceRepository(): VoiceRepository {
        return VoiceRepositoryImpl(
            apiService = ApiModule.provideVoiceApi(),
            webSocketClient = WebSocketClient(NetworkModule.provideOkHttp())
        )
    }
}