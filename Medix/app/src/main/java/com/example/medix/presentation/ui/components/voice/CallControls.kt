package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CallControls(
    isMicPressed: Boolean,
    isMuted: Boolean,
    onSpeaker: () -> Unit,
    onMicHoldStart: () -> Unit,
    onMicHoldEnd: () -> Unit,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        HoldControlButton(
            icon = if (isMicPressed) Icons.Default.Mic else Icons.Default.MicOff,
            label = "Micrófono",
            onHoldStart = onMicHoldStart,
            onHoldEnd = onMicHoldEnd,
        )

        ControlButton(
            icon = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
            label = "Audio",
            onClick = onSpeaker
        )
    }
}


@Composable
private fun HoldControlButton(
    icon: ImageVector,
    label: String,
    onHoldStart: () -> Unit,
    onHoldEnd: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(110.dp)
    ) {

        Box(
            modifier = Modifier
                .size(64.dp) // WCAG touch target
                .background(Color.LightGray, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onHoldStart()
                            tryAwaitRelease()
                            onHoldEnd()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp
        )
    }
}


@Composable
fun ControlButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(110.dp)
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.LightGray, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures { onClick() }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp
        )
    }
}