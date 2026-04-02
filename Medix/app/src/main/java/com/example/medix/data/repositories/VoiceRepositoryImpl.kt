package com.example.medix.data.repositories

import android.util.Log
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
        Log.d("VoiceRepository", "Transcribing audio: ${audioFile.name}, size: ${audioFile.length()} bytes")
        val requestBody = audioFile.asRequestBody("audio/mp4".toMediaType())
        val part = MultipartBody.Part.createFormData("file", audioFile.name, requestBody)

        return try {
            val response = apiService.transcribeAudio(part)
            Log.i("VoiceRepository", "Transcription success: ${response.text.take(50)}")
            response.text
        } catch (e: Exception) {
            Log.e("VoiceRepository", "Transcription failed", e)
            throw e
        }
    }

    override suspend fun sendConversationMessage(
        text: String,
        sessionId: String
    ): ConversationResponse {
        Log.d("VoiceRepository", "Sending conversation message: '${text.take(50)}', sessionId: $sessionId")
        val request = ConversationRequest(
            text = text,
            session_id = sessionId
        )

        return try {
            val response = apiService.sendMessage(request)
            Log.i("VoiceRepository", "Conversation response: '${response.response.take(50)}...', completed: ${response.completed}")
            response
        } catch (e: Exception) {
            Log.e("VoiceRepository", "Conversation request failed", e)
            throw e
        }
    }

    override fun connectWebSocket(
        sessionId: String,
        onMessage: (String) -> Unit,
        onStateChanged: (Boolean) -> Unit,
    ) {
        val url = Constants.webSocketUrl(sessionId)
        Log.d("VoiceRepository", "Connecting WebSocket: $url")

        webSocketClient.connect(
            url,
            onMessage = { payload ->
                Log.d("VoiceRepository", "WebSocket message handler called")
                onMessage(payload)
            },
            onStateChanged = { connected ->
                Log.i("VoiceRepository", "WebSocket state: $connected")
                onStateChanged(connected)
            }
        )
    }

    override fun sendWebSocketMessage(
        text: String,
        sessionId: String
    ): Boolean {
        Log.d("VoiceRepository", "Sending WebSocket message: '${text.take(50)}', sessionId: $sessionId")
        val payload = JSONObject()
            .put("text", text)
            .put("session_id", sessionId)
            .toString()

        val success = webSocketClient.send(payload)
        if (success) {
            Log.i("VoiceRepository", "WebSocket message sent successfully")
        } else {
            Log.w("VoiceRepository", "WebSocket message send failed (WebSocket not ready)")
        }
        return success
    }

    override fun closeWebSocket() {
        Log.d("VoiceRepository", "Closing WebSocket")
        webSocketClient.close()
    }
}