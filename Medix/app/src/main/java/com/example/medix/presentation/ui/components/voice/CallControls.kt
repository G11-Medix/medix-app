package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CallControls(
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    onMute: () -> Unit,
    onSpeaker: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {


        ControlButton(
            icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
            label = if (isMuted) "Activar" else "Silenciar",
            onClick = onMute
        )


        ControlButton(
            icon = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
            label = if (isSpeakerOn) "Altavoz" else "Altavoz",
            onClick = onSpeaker
        )
    }
}

@Composable
fun ControlButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.LightGray, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(label, fontSize = 10.sp)
    }
}