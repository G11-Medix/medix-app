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
import androidx.compose.ui.text.style.TextAlign
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
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            Color(0xFF1B5E20),
            Icons.Default.CheckCircle,
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
            Icons.Default.Cancel,
            "Confirmación cancelada"
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = desc,
                tint = iconColor,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

