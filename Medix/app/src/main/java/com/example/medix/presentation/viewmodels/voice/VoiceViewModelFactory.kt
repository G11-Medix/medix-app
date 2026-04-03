package com.example.medix.presentation.viewmodels.voice

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medix.domain.repositories.VoiceRepository
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder

class VoiceViewModelFactory(
    private val repository: VoiceRepository,
    private val recorder: AudioRecorder,
    private val player: AudioPlayer,
    private val context: Context,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoiceViewModel(
                repository,
                recorder,
                player,
                context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}