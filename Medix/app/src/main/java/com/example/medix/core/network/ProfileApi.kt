package com.example.medix.core.network

import com.example.medix.data.dto.UserProfileDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("profile")
    suspend fun getProfile(): UserProfileDto
}