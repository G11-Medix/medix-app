package com.example.medix.presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat

import com.example.medix.presentation.ui.components.voice.*
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel

private const val DEFAULT_SCHEDULING_PROMPT = "Hola Medix"

@Composable
fun VoiceScreen(
    viewModel: VoiceViewModel,
    onEndCall: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val context = LocalContext.current

    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMicPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasMicPermission) {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
        viewModel.startSchedulingSession(DEFAULT_SCHEDULING_PROMPT)
    }

    val state by viewModel.uiState.collectAsState()

    var isMicPressed by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        if (isLandscape) {
            // 🔹 LANDSCAPE
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //TopBar()

                    Spacer(Modifier.height(16.dp))

                    ActiveSessionHeader(state.status)

                    Spacer(Modifier.height(16.dp))

                    AudioVisualizer(state.status)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        TranscriptCard(
                            "Tu mensaje:",
                            state.userText.ifBlank { "Aún no hay transcripción" }
                        )

                        Spacer(Modifier.height(12.dp))

                        TranscriptCard(
                            "Asistente Medix:",
                            state.assistantText
                        )
                    }

                    CallControls(
                        isMicPressed = isMicPressed,
                        isMuted = isMuted,
                        onSpeaker = {
                            isMuted = !isMuted
                            viewModel.toggleMute()
                        },
                        onMicHoldStart = {
                            isMicPressed = true
                            if (hasMicPermission) viewModel.startRecording()
                        },
                        onMicHoldEnd = {
                            isMicPressed = false
                            if (hasMicPermission) viewModel.stopRecordingAndSend()
                        }
                    )

                    EndCallButton(onEndCall)
                }
            }

        } else {
            // 🔹 PORTRAIT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //TopBar()

                Spacer(Modifier.height(16.dp))

                ActiveSessionHeader(state.status)

                Spacer(Modifier.height(24.dp))

                AudioVisualizer(state.status)

                Spacer(Modifier.height(24.dp))

                TranscriptCard(
                    "Tu mensaje:",
                    state.userText.ifBlank { "Aún no hay transcripción" }
                )

                Spacer(Modifier.height(16.dp))

                TranscriptCard(
                    "Asistente Medix:",
                    state.assistantText
                )

                Spacer(Modifier.weight(1f))

                CallControls(
                    isMicPressed = isMicPressed,
                    isMuted = isMuted,
                    onSpeaker = {
                        isMuted = !isMuted
                        viewModel.toggleMute()
                    },
                    onMicHoldStart = {
                        isMicPressed = true
                        if (hasMicPermission) viewModel.startRecording()
                    },
                    onMicHoldEnd = {
                        isMicPressed = false
                        if (hasMicPermission) viewModel.stopRecordingAndSend()
                    }
                )

                EndCallButton(onEndCall)
            }
        }
    }
}