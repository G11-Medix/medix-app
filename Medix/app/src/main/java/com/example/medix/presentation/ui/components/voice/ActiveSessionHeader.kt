package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
        ConversationStatus.LISTENING -> Color(0xFF2E7D32) to "Escuchando"
        ConversationStatus.RESPONDING -> Color(0xFF1565C0) to "Hablando"
        ConversationStatus.PROCESSING -> Color(0xFFF9A825) to "Procesando"
        ConversationStatus.ERROR -> Color(0xFFC62828) to "Error"
        else -> Color.Gray to "Conectando"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sesión activa",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        Icon(
            imageVector = Icons.Default.HealthAndSafety,
            contentDescription = "Estado del asistente",
            tint = Color(0xFF1565C0),
            modifier = Modifier.size(80.dp) //
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Asistente de voz activo",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}