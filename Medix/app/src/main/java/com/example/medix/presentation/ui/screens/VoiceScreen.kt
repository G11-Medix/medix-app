package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medix.presentation.ui.components.ActiveSessionHeader
import com.example.medix.presentation.ui.components.AudioVisualizer
import com.example.medix.presentation.ui.components.CallControls
import com.example.medix.presentation.ui.components.EndCallButton
import com.example.medix.presentation.ui.components.TopBar
import com.example.medix.presentation.ui.components.TranscriptCard

import kotlinx.coroutines.delay

@Composable
fun VoiceScreen(
    onEndCall: () -> Unit
) {

    var callState by remember { mutableStateOf("Connecting") }
    var transcript by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        delay(2000)
        callState = "Listening"

        delay(3000)
        callState = "Speaking"
        transcript = "Me gustaría programar una cita..."

        delay(4000)
        transcript = "con el Dr. Smith para el próximo martes..."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBar()

        Spacer(modifier = Modifier.height(16.dp))

        ActiveSessionHeader(callState)

        Spacer(modifier = Modifier.height(24.dp))

        AudioVisualizer(callState)

        Spacer(modifier = Modifier.height(24.dp))

        TranscriptCard(transcript)

        Spacer(modifier = Modifier.weight(1f))

        CallControls()

        EndCallButton(
            onEndCall = onEndCall
        )
    }
}