package com.example.medix.di


import com.example.medix.core.network.ApiService
import com.example.medix.core.network.AppointmentApi
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
}