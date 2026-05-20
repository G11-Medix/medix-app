package com.example.medix.presentation.ui.components.records

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getStateColor(state: String): Color {
    return when (state.trim().lowercase()) {
        "scheduled" -> Color(0xFF4CAF50)
        "cancelled" -> Color(0xFFF44336)
        "completed" -> Color(0xFF2196F3) // azul
        "attended" -> Color(0xFF2196F3)
        else -> MaterialTheme.colorScheme.primary
    }
}


fun formatState(state: String): String {
    return when (state.trim().lowercase()) {
        "scheduled" -> "Agendada"
        "cancelled" -> "Cancelada"
        "completed" -> "Finalizada"
        "attended" -> "Atendida"
        else -> state
    }
}

fun getVisualState(
    state: String,
    isPast: Boolean
): String {

    return when {
        isPast && state.lowercase() == "scheduled" -> "completed"
        else -> state
    }
}