package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.status.ConversationStatus

@Composable
fun AudioVisualizer(status: ConversationStatus) {
    // Solo animar cuando el asistente está hablando o procesando
    val active = status == ConversationStatus.PROCESSING || status == ConversationStatus.RESPONDING
    
    val bars = when (status) {
        ConversationStatus.PROCESSING -> 6
        ConversationStatus.RESPONDING -> 7
        ConversationStatus.LISTENING -> 5
        else -> 4
    }
    
    val transition = rememberInfiniteTransition(label = "audio_visualizer")

    val containerColor = when (status) {
        ConversationStatus.ERROR -> Color(0xFFFFEBEE)
        ConversationStatus.PROCESSING -> Color(0xFFE3F2FD)
        ConversationStatus.RESPONDING -> Color(0xFFE8F5E9)
        ConversationStatus.LISTENING -> Color(0xFFE3F2FD)
        else -> Color(0xFFF3F6FB)
    }
    val barColor = when (status) {
        ConversationStatus.ERROR -> Color(0xFFC62828)
        ConversationStatus.RESPONDING -> Color(0xFF2E7D32)
        else -> Color(0xFF1565C0)
    }

    // Contenedor con altura fija para evitar saltos en la UI
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // Altura fija garantizada
            .padding(horizontal = 32.dp)
            .background(containerColor, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.height(40.dp), // Area interna fija para las barras
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(bars) { index ->
                val duration = if (active) 650 + (index * 40) else 1200
                val delay = index * 80
                
                val pulse by transition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = if (active) 1f else 0.2f, // Stay at bottom if not active
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = duration, delayMillis = delay),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse_$index"
                )

                // Si no está activo, las barras se quedan en su altura mínima y no se mueven
                val minHeight = 12.dp
                val maxHeight = if (status == ConversationStatus.RESPONDING) 38.dp else 28.dp
                
                // Forzar altura fija mínima cuando no hay actividad
                val height = if (active) {
                    minHeight + ((maxHeight - minHeight) * pulse)
                } else {
                    minHeight
                }

                Box(
                    modifier = Modifier
                        .width(9.dp)
                        .height(height)
                        .background(barColor, RoundedCornerShape(3.dp))
                )
            }
        }
    }
}
