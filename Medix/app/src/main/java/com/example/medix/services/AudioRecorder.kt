package com.example.medix.services

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    private var isPaused = false

    fun start(): File {
        val file = File.createTempFile("medix_record_", ".m4a", context.cacheDir)
        outputFile = file

        recorder = MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(96_000)
            setAudioSamplingRate(44_100)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        isPaused = false
        return file
    }

    fun stop(): File? {
        return try {
            recorder?.apply {
                stop()
                release()
            }
            outputFile
        } catch (_: Exception) {
            null
        } finally {
            recorder = null
            isPaused = false
        }
    }

    fun mute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recorder?.let {
                if (!isPaused) {
                    runCatching {
                        it.pause()
                        isPaused = true
                    }
                }
            }
        }
    }

    fun unmute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recorder?.let {
                if (isPaused) {
                    runCatching {
                        it.resume()
                        isPaused = false
                    }
                }
            }
        }
    }


    fun isRecording(): Boolean {
        return recorder != null && !isPaused
    }

    fun isMuted(): Boolean {
        return isPaused
    }
}