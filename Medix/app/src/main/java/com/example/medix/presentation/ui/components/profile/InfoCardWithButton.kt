package com.example.medix.presentation.ui.components.profile
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.theme.TextSecondary

@Composable
fun InfoCardWithButton(
    icon: ImageVector,
    title: String,
    value: String,
    buttonText: String,
    iconContentDescription: String = title // ✅ ACCESIBILIDAD: Descripción del ícono
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ ACCESIBILIDAD: contentDescription para lector de pantalla
            Icon(
                icon,
                contentDescription = iconContentDescription,
                tint = LocalTextStyle.current.color
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // ✅ ACCESIBILIDAD: Reemplazado Color.Gray (bajo contraste) con TextSecondary (4.5+:1)
                Text(
                    title,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Text(value, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}