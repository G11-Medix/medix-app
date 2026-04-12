package com.example.medix.core.network

import android.util.Log
import com.example.medix.core.auth.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WebSocketClient @Inject constructor(
    private val sessionManager: SessionManager,
    private val client: OkHttpClient
) {

    private var webSocket: WebSocket? = null
    private var reconnectJob: Job? = null
    private var latestUrl: String? = null
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 10

    fun connect(
        url: String,
        onMessage: (String) -> Unit,
        onStateChanged: (Boolean) -> Unit,
    ) {
        Log.d("WebSocketClient", "Connecting to: $url")
        latestUrl = url
        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.i("WebSocketClient", "✓ WebSocket connected: ${response.code}")
                    reconnectJob?.cancel()
                    reconnectJob = null
                    reconnectAttempts = 0
                    onStateChanged(true)
                    CoroutineScope(Dispatchers.IO).launch {
                        val idPaciente = sessionManager.getPacienteId()

                        if (idPaciente != null) {
                            val initMessage = """
                                {
                                    "type": "init",
                                    "id_paciente": $idPaciente
                                }
                            """.trimIndent()

                            webSocket.send(initMessage)
                        }
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d("WebSocketClient", "→ Received message: $text")
                    try {
                        onMessage(text)
                    } catch (e: Exception) {
                        Log.e("WebSocketClient", "Error processing message", e)
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.w("WebSocketClient", "⚠ Closing: code=$code, reason=$reason")
                    onStateChanged(false)
                    webSocket.close(code, reason)
                    scheduleReconnect(onMessage, onStateChanged)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("WebSocketClient", "✗ Connection failed", t)
                    onStateChanged(false)
                    scheduleReconnect(onMessage, onStateChanged)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    onStateChanged(false)
                    scheduleReconnect(onMessage, onStateChanged)
                }
            },
        )
    }

    fun send(text: String): Boolean {
        val result = webSocket?.send(text) ?: false
        if (result) {
            Log.d("WebSocketClient", "← Sent message: $text")
        } else {
            Log.w("WebSocketClient", "⚠ Failed to send message (WebSocket null or closed)")
        }
        return result
    }

    fun close() {
        Log.d("WebSocketClient", "Closing WebSocketClient")
        reconnectJob?.cancel()
        reconnectJob = null
        try {
            webSocket?.close(1000, "Closed by client")
        } catch (e: Exception) {
            Log.e("WebSocketClient", "Error closing WebSocket", e)
        }
        webSocket = null
    }

    private fun scheduleReconnect(
        onMessage: (String) -> Unit,
        onStateChanged: (Boolean) -> Unit,
    ) {
        if (reconnectJob?.isActive == true) {
            Log.d("WebSocketClient", "Reconnect already scheduled")
            return
        }
        val url = latestUrl ?: return

        reconnectAttempts++
        if (reconnectAttempts > maxReconnectAttempts) {
            Log.e("WebSocketClient", "Max reconnection attempts ($maxReconnectAttempts) reached")
            return
        }

        // Exponential backoff: 2.5s, 5s, 10s, 20s, etc.
        val delayMs = (RECONNECT_DELAY_MS * reconnectAttempts).coerceAtMost(30_000L)
        Log.i("WebSocketClient", "Scheduling reconnection attempt $reconnectAttempts in ${delayMs}ms")

        reconnectJob = CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            Log.d("WebSocketClient", "Attempting to reconnect (attempt $reconnectAttempts)")
            connect(url, onMessage, onStateChanged)
        }
    }

    private companion object {
        const val RECONNECT_DELAY_MS = 2_500L
    }
}
