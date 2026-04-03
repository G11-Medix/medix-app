package com.example.medix.domain.repositories

import com.example.medix.data.dto.ConversationResponse
import java.io.File

interface VoiceRepository {

    suspend fun transcribeAudio(audioFile: File): String

    suspend fun sendConversationMessage(
        text: String,
        sessionId: String
    ): ConversationResponse

    fun connectWebSocket(
        sessionId: String,
        onMessage: (String) -> Unit,
        onStateChanged: (Boolean) -> Unit,
    )

    fun sendWebSocketMessage(
        text: String,
        sessionId: String
    ): Boolean

    fun closeWebSocket()
}