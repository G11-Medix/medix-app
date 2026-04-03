package com.example.medix.presentation.ui.components.voice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.viewmodels.status.ConversationStatus

@Composable
fun ActiveSessionHeader(status: ConversationStatus) {

    val (color, title) = when (status) {
        ConversationStatus.LISTENING -> Color.Green to "Escuchando..."
        ConversationStatus.RESPONDING -> Color(0xFF1E88E5) to "Hablando..."
        ConversationStatus.PROCESSING -> Color(0xFFFFA000) to "Procesando..."
        ConversationStatus.ERROR -> Color.Red to "Error"
        else -> Color.Gray to "Conectando..."
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Sesión activa",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.HealthAndSafety,
                contentDescription = null,
                tint = Color(0xFF1565C0),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Asistente de voz activo",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}