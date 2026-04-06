package com.example.medix.di

import com.example.medix.core.network.PacienteApiService
import com.example.medix.core.network.WebSocketClient
import com.example.medix.data.repositories.AppointmentRepositoryImpl
import com.example.medix.data.repositories.ConfirmationRepositoryImpl
import com.example.medix.data.repositories.PacienteRepositoryImpl
import com.example.medix.data.repositories.ProfileRepositoryImpl
import com.example.medix.data.repositories.VoiceRepositoryImpl
import com.example.medix.domain.repositories.AppointmentRepository
import com.example.medix.domain.repositories.ConfirmationRepository
import com.example.medix.domain.repositories.PacienteRepository
import com.example.medix.domain.repositories.ProfileRepository
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

    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepositoryImpl(
            ApiModule.provideProfileApi()
        )
    }

    fun provideConfirmationRepository(): ConfirmationRepository {
        return ConfirmationRepositoryImpl(
            ApiModule.provideConfirmationApi()
        )
    }

    fun providePacienteRepository(): PacienteRepository {
        return PacienteRepositoryImpl(
            apiService = ApiModule.providePacienteApiService()
        )
    }
}