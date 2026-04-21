package com.example.medix.presentation.ui.components.records

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getStateColor(state: String): Color {
    return when (state.lowercase()) {
        "scheduled" -> Color(0xFF4CAF50)
        "cancelled" -> Color(0xFFF44336)
        else -> MaterialTheme.colorScheme.primary
    }
}