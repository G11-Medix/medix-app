package com.example.medix.services

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun start(): File {
        // Si había una sesión previa incompleta, limpiarla antes de iniciar otra.
        releaseRecorder()

        val file = File.createTempFile("medix_record_", ".m4a", context.cacheDir)
        outputFile = file

        val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        recorder = mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(96_000)
            setAudioSamplingRate(44_100)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        return file
    }

    fun stop(): File? {
        val currentRecorder = recorder ?: return null
        val file = outputFile

        return try {
            currentRecorder.stop()
            file?.takeIf { it.exists() && it.length() > 0L }
        } catch (_: Exception) {
            // Si stop() falla (p.ej. grabacion demasiado corta), descartar el archivo.
            file?.delete()
            null
        } finally {
            releaseRecorder()
            outputFile = null
        }
    }

    fun mute() = Unit

    fun unmute() = Unit

    private fun releaseRecorder() {
        runCatching { recorder?.reset() }
        runCatching { recorder?.release() }
        recorder = null
    }
}