package com.example.medix.core.network

import com.example.medix.data.dto.LoginRequest
import com.example.medix.data.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @Headers(
        "Content-Type: application/json",
        "apikey: TU_SUPABASE_ANON_KEY"
    )
    @POST("auth/v1/token?grant_type=password")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}