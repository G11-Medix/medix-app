package com.example.medix.di


import com.example.medix.core.network.ApiService
import com.example.medix.core.network.AppointmentApi
import com.example.medix.core.network.ConfirmationApi
import com.example.medix.core.network.PacienteApiService
import com.example.medix.core.network.ProfileApi
import retrofit2.Retrofit


object ApiModule {

    fun provideAppointmentApi(): AppointmentApi {
        return NetworkModule
            .provideRetrofit()
            .create(AppointmentApi::class.java)
    }

    fun provideVoiceApi(): ApiService {
        return NetworkModule
            .provideRetrofit()
            .create(ApiService::class.java)
    }

    fun provideProfileApi(): ProfileApi {
        return NetworkModule
            .provideRetrofit()
            .create(ProfileApi::class.java)
    }

    fun provideConfirmationApi(): ConfirmationApi {
        return NetworkModule
            .provideRetrofit()
            .create(ConfirmationApi::class.java)
    }

    fun providePacienteApiService(): PacienteApiService {
        return NetworkModule
            .provideRetrofitAuth()
            .create(PacienteApiService::class.java)
    }
}