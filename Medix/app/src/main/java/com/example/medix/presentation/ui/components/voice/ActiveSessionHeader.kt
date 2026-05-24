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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.medix.presentation.viewmodels.status.ConversationStatus
import com.example.medix.presentation.ui.theme.TextSecondary
import com.example.medix.presentation.ui.theme.SuccessGreen
import com.example.medix.presentation.ui.theme.InfoBlue
import com.example.medix.presentation.ui.theme.WarningOrange
import com.example.medix.presentation.ui.theme.ErrorRed

@Composable
fun ActiveSessionHeader(status: ConversationStatus) {

    // ✅ ACCESIBILIDAD: Usar colores accesibles verificados
    val (color, title) = when (status) {
        ConversationStatus.LISTENING -> SuccessGreen to "Escuchando"
        ConversationStatus.RESPONDING -> InfoBlue to "Hablando"
        ConversationStatus.PROCESSING -> WarningOrange to "Procesando"
        ConversationStatus.ERROR -> ErrorRed to "Error"
        else -> TextSecondary to "Conectando"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        // ✅ ACCESIBILIDAD: Agregar descripción general
        modifier = Modifier.semantics {
            contentDescription = "Sesión activa, estado: $title"
        }
    ) {

        Text(
            text = "Sesión activa",
            fontSize = 14.sp,
            // ✅ ACCESIBILIDAD: Reemplazar Color.Gray con TextSecondary (contraste 4.5+:1)
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Icon(
            imageVector = Icons.Default.HealthAndSafety,
            contentDescription = "Estado del asistente: $title",
            tint = InfoBlue,
            modifier = Modifier.size(80.dp)
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
                // ✅ ACCESIBILIDAD: Reemplazar Color.Gray con TextSecondary (contraste 4.5+:1)
                color = TextSecondary
            )
        }
    }
}