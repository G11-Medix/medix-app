package com.example.medix.di

import com.example.medix.core.network.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthModule {

    fun provideAuthApi(): AuthApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://TU_PROJECT.supabase.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApi::class.java)
    }
}