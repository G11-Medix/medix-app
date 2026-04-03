package com.example.medix.presentation.ui.components.voice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.status.ConversationStatus

@Composable
fun AudioVisualizer(status: ConversationStatus) {

    val bars = when (status) {
        ConversationStatus.RESPONDING -> 8
        ConversationStatus.LISTENING -> 5
        else -> 3
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(Color(0xFFE3F2FD), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        repeat(bars) {
            val height = (20..60).random()

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(height.dp)
                    .background(Color(0xFF1565C0))
            )
        }
    }
}