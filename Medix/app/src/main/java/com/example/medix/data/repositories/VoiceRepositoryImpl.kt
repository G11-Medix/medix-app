package com.example.medix.data.repositories



import com.example.medix.core.network.ApiService
import com.example.medix.core.network.WebSocketClient
import com.example.medix.core.utils.Constants
import com.example.medix.data.dto.ConversationRequest
import com.example.medix.data.dto.ConversationResponse
import com.example.medix.domain.repositories.VoiceRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class VoiceRepositoryImpl(
    private val apiService: ApiService,
    private val webSocketClient: WebSocketClient,
) : VoiceRepository {

    override suspend fun transcribeAudio(audioFile: File): String {
        val requestBody = audioFile
            .asRequestBody("audio/mp4".toMediaType())

        val part = MultipartBody.Part.createFormData(
            "audio",
            audioFile.name,
            requestBody
        )

        return apiService.transcribeAudio(part).text
    }

    override suspend fun sendConversationMessage(
        text: String,
        sessionId: String
    ): ConversationResponse {
        return apiService.sendMessage(
            ConversationRequest(
                text = text,
                session_id = sessionId
            )
        )
    }

    override fun connectWebSocket(
        sessionId: String,
        onMessage: (String) -> Unit,
        onStateChanged: (Boolean) -> Unit,
    ) {
        webSocketClient.connect(
            Constants.webSocketUrl(sessionId),
            onMessage,
            onStateChanged
        )
    }

    override fun sendWebSocketMessage(
        text: String,
        sessionId: String
    ) {
        val payload = JSONObject()
            .put("text", text)
            .put("session_id", sessionId)
            .toString()

        webSocketClient.send(payload)
    }

    override fun closeWebSocket() {
        webSocketClient.close()
    }
}