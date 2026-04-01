package com.example.medix.core.utils

object Constants {
    const val BASE_URL = "http://10.43.101.9:8000"

    fun webSocketUrl(sessionId: String): String =
        "ws://10.43.101.9:8000/ws/conversation/$sessionId"
}
