package com.example.medix.presentation.ui.components.records

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getStateColor(state: String): Color {
    val s = state.trim().lowercase()
    return when {
        s == "scheduled" -> Color(0xFF4CAF50)
        s in setOf("cancelled", "canceled", "cancelada", "cancelado") -> Color(0xFFF44336)
        s in setOf("completed", "attended") -> Color(0xFF2196F3)
        else -> MaterialTheme.colorScheme.primary
    }
}

fun formatState(state: String): String {
    val s = state.trim().lowercase()
    return when {
        s == "scheduled" -> "Agendada"
        s in setOf("cancelled", "canceled", "cancelada", "cancelado") -> "Cancelada"
        s == "completed" -> "Finalizada"
        s == "attended" -> "Atendida"
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