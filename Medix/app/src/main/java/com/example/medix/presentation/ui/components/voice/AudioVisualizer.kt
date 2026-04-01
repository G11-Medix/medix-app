package com.example.medix.presentation.ui.components.voice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioVisualizer(state: String) {

    val bars = if (state == "Speaking") 8 else 3

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