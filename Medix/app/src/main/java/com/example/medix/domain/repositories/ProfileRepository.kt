package com.example.medix.domain.repositories

import com.example.medix.data.dto.UserProfileDto

interface ProfileRepository {

    suspend fun getProfile(): UserProfileDto
}