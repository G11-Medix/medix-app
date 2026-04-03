package com.example.medix.di


import com.example.medix.core.auth.AuthInterceptor
import com.example.medix.core.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object NetworkModule {

    fun provideOkHttp(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(provideOkHttp())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}