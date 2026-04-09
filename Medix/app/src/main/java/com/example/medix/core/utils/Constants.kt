package com.example.medix.core.utils

object Constants {
    const val BASE_URL = ""

    fun webSocketUrl(sessionId: String): String =
        "ws://192.168.20.10:8000/ws/conversation/$sessionId"
}
