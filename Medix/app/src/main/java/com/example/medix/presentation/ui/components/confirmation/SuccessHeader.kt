package com.example.medix.presentation.ui.components.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


data class StatusUI(
    val bgColor: Color,
    val iconColor: Color,
    val textColor: Color,
    val icon: ImageVector,
    val description: String
)

@Composable
fun SuccessHeader(
    title: String,
    message: String,
    status: String
) {
    val (bgColor, iconColor, textColor, icon, desc) = when (status) {
        "SUCCESS" -> StatusUI(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            Icons.Default.Check,
            "Confirmación exitosa"
        )
        "PENDING" -> StatusUI(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondary,
            Icons.Default.Schedule,
            "Confirmación pendiente"
        )
        else -> StatusUI(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.error,
            Icons.Default.Close,
            "Confirmación cancelada"
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = desc,
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

