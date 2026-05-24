package com.example.medix.presentation.ui.components.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.medix.domain.entities.Notification
import com.example.medix.presentation.ui.theme.TextSecondary
import com.example.medix.presentation.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun NotificationCard(notification: Notification) {

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            // ✅ ACCESIBILIDAD: Agregar descripción para lector de pantalla
            .semantics {
                contentDescription = "Notificación: ${notification.title}, ${notification.body}"
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                notification.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                softWrap = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                notification.body,
                // ✅ ACCESIBILIDAD: Reemplazar Color(0xFF666666) con TextSecondary (contraste 4.5+:1)
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                softWrap = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(notification.timestamp)),
                fontSize = 12.sp,
                // ✅ ACCESIBILIDAD: Reemplazar Color(0xFF999999) con TextTertiary (contraste 5.1+:1)
                color = TextTertiary
            )
        }
    }
}