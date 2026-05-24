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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.status.ConversationStatus

@Composable
fun AudioVisualizer(status: ConversationStatus) {
    val active = status == ConversationStatus.PROCESSING || status == ConversationStatus.RESPONDING
    val bars = when (status) {
        ConversationStatus.PROCESSING -> 6
        ConversationStatus.RESPONDING -> 7
        ConversationStatus.LISTENING -> 5
        else -> 4
    }
    val transition = rememberInfiniteTransition(label = "audio_visualizer")
    val pulse by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = if (active) 1f else 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (active) 650 else 1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

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

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        repeat(bars) { index ->
            val phase = ((index + 1) * 0.18f)
            val normalized = (pulse + phase).coerceAtMost(1f)
            val minHeight = if (active) 22.dp else 16.dp
            val maxHeight = if (status == ConversationStatus.RESPONDING) 64.dp else 52.dp
            val height = minHeight + ((maxHeight - minHeight) * normalized)

            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(height)
                    .background(barColor)
            )
        }
    }
}