package com.example.medix.core.utils

import com.example.medix.BuildConfig

object Constants {
    const val BASE_URL = BuildConfig.MEDIX_DATA_API_BASE_URL

    fun webSocketUrl(sessionId: String): String =
        "${BuildConfig.MEDIX_WS_BASE_URL.trimEnd('/')}/ws/conversation/$sessionId"
}
