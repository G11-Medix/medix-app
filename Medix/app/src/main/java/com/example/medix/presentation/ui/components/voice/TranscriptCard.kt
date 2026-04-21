package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TranscriptCard(title: String, text: String) {

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2FB)),
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(8.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = formatTranscript(text),
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}


fun formatTranscript(text: String): String {
    var formatted = text.trim()

    // Salto después de ":" si viene contenido
    formatted = formatted.replace(Regex(":\\s*"), ":\n")

    //Listas numeradas → salto antes de "1. 2. 3."
    formatted = formatted.replace(Regex("(\\d+)\\.\\s"), "\n$1. ")

    //  Fechas tipo 2026-04-05 → cada una en línea nueva
    formatted = formatted.replace(Regex("(\\d{4}-\\d{2}-\\d{2})"), "\n$1")

    // uitar saltos duplicados
    formatted = formatted.replace(Regex("\n+"), "\n")

    return formatted.trim()
}