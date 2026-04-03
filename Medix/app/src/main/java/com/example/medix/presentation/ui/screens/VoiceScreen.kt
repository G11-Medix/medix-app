package com.example.medix.presentation.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat

import com.example.medix.presentation.ui.components.voice.*
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel

private const val DEFAULT_SCHEDULING_PROMPT = "Quiero empezar el agendamiento de una cita médica."

@Composable
fun VoiceScreen(
    viewModel: VoiceViewModel,
    onEndCall: () -> Unit
) {

    // Permisos

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

    // Estado

    val state by viewModel.uiState.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val isSpeakerOn by viewModel.isSpeakerOn.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBar()

        Spacer(modifier = Modifier.height(16.dp))

        ActiveSessionHeader(state.status)

        Spacer(modifier = Modifier.height(24.dp))

        AudioVisualizer(state.status)

        Spacer(modifier = Modifier.height(24.dp))

        TranscriptCard(
            "Tu mensaje:",
            state.userText.ifBlank { "Aún no hay transcripción" }
        )

        Spacer(modifier = Modifier.height(24.dp))

        TranscriptCard(
            "Asistente Medix:",
            state.assistantText
        )

        Spacer(modifier = Modifier.weight(1f))

        if (!hasMicPermission) {
            Text(
                text = "Debes conceder permiso de micrófono",
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                }
            ) {
                Text("Conceder permiso")
            }
        }

        CallControls(
            onSpeaker = {
                viewModel.toggleSpeaker()
            },
            onMicHoldStart = {
                if (hasMicPermission) {
                    viewModel.startRecording()
                } else {
                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                }
            },
            onMicHoldEnd = {
                if (hasMicPermission) {
                    viewModel.stopRecordingAndSend()
                }
            }
        )

        EndCallButton(
            onEndCall = onEndCall
        )
    }
}