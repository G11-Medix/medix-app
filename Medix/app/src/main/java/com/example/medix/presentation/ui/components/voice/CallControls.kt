package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.example.medix.presentation.ui.theme.ButtonSecondary

@Composable
fun CallControls(
    isMicPressed: Boolean,
    onMicHoldStart: () -> Unit,
    onMicHoldEnd: () -> Unit,
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        // Only show the microphone control (larger and centered) — audio button removed by spec
        HoldControlButton(
            icon = if (isMicPressed) Icons.Default.Mic else Icons.Default.MicOff,
            isActive = isMicPressed,
            onHoldStart = onMicHoldStart,
            onHoldEnd = onMicHoldEnd,
        )
    }
}


@Composable
private fun HoldControlButton(
    icon: ImageVector,
    isActive: Boolean,
    onHoldStart: () -> Unit,
    onHoldEnd: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(140.dp)
    ) {

        Box(
            modifier = Modifier
                .size(80.dp) // agrandado para facilitar toque
                // ✅ ACCESIBILIDAD: Reemplazar Color.LightGray con ButtonSecondary (contraste 3:1+)
                .background(ButtonSecondary, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onHoldStart()
                            tryAwaitRelease()
                            onHoldEnd()
                        }
                    )
                }
                // ✅ ACCESIBILIDAD: Agregar semántica para lector de pantalla
                .semantics {
                    contentDescription = "Micrófono ${if (isActive) "activo" else "inactivo"}"
                    role = Role.Button
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null, // Padre tiene descripción
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Micrófono",
            fontSize = 14.sp
        )
    }
}


@Composable
fun ControlButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(110.dp)
    ) {

        Box(
            modifier = Modifier
                .size(64.dp) // ✅ WCAG touch target mínimo 48x48dp
                // ✅ ACCESIBILIDAD: Reemplazar Color.LightGray con ButtonSecondary (contraste 3:1+)
                .background(ButtonSecondary, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures { onClick() }
                }
                // ✅ ACCESIBILIDAD: Agregar semántica para lector de pantalla
                .semantics {
                    contentDescription = "$label ${if (isActive) "activo" else "inactivo"}"
                    role = Role.Button
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null, // Padre tiene descripción
                tint = Color.White
            )

        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp
        )
    }
}