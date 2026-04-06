package com.example.medix.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class AudioPlayer(context: Context) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private var pendingText: String? = null
    private var pendingId: String? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d("AudioPlayer", "TextToSpeech initialized successfully")
            val result = textToSpeech.setLanguage(Locale.forLanguageTag("es-ES"))
            val languageOk =
                result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED

            if (languageOk) {
                textToSpeech.setSpeechRate(0.95f)
                textToSpeech.setPitch(1.0f)
                isReady = true
                Log.d("AudioPlayer", "Ready to speak")
                flushPendingText()
            } else {
                Log.w("AudioPlayer", "Language not supported: es-ES, code: $result")
            }
        } else {
            Log.e("AudioPlayer", "TextToSpeech initialization failed with status: $status")
        }
    }

    fun speak(text: String, isMuted: Boolean) {
        if (isMuted) {
            Log.d("AudioPlayer", "Muted → not speaking")
            return
        }

        if (text.isBlank()) {
            Log.w("AudioPlayer", "Attempting to speak blank text")
            return
        }

        if (!isReady) {
            Log.w("AudioPlayer", "TextToSpeech not ready, queuing text")
            pendingText = text
            pendingId = java.util.UUID.randomUUID().toString()
            return
        }

        val chunks = splitForTts(text)
        var first = true

        chunks.forEachIndexed { index, chunk ->
            val queueMode = if (first) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
            val utteranceId = "MEDIX_TTS_${System.currentTimeMillis()}_$index"

            val result = textToSpeech.speak(chunk, queueMode, null, utteranceId)

            if (result == TextToSpeech.ERROR) {
                Log.e("AudioPlayer", "speak() returned ERROR, queuing full text")
                pendingText = text
                pendingId = java.util.UUID.randomUUID().toString()
                return
            }

            first = false
        }

        pendingText = null
        pendingId = null
    }

    fun release() {
        isReady = false
        pendingText = null
        pendingId = null
        try {
            textToSpeech.stop()
            textToSpeech.shutdown()
            Log.d("AudioPlayer", "AudioPlayer released")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error releasing TextToSpeech", e)
        }
    }

    private fun flushPendingText() {
        val text = pendingText ?: return
        val token = pendingId ?: return
        Log.d("AudioPlayer", "Flushing pending text: $text")
        pendingText = null
        pendingId = null
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, token)
    }

    private fun splitForTts(text: String): List<String> {
        val normalized = text.trim()
        if (normalized.length <= MAX_TTS_CHARS) return listOf(normalized)

        val chunks = mutableListOf<String>()
        var start = 0
        while (start < normalized.length) {
            val end = (start + MAX_TTS_CHARS).coerceAtMost(normalized.length)
            val nextBreak = normalized.lastIndexOfAny(charArrayOf('.', ',', ';', ':', ' '), end - 1)
            val splitAt = if (nextBreak > start + MAX_TTS_CHARS / 2) nextBreak + 1 else end
            chunks += normalized.substring(start, splitAt).trim()
            start = splitAt
        }
        return chunks.filter { it.isNotBlank() }
    }

    private companion object {
        const val MAX_TTS_CHARS = 220
    }
}