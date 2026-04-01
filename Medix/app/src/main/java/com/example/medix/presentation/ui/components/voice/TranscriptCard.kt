package com.example.medix.presentation.ui.components.voice
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TranscriptCard(text: String) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2FB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = " Lo que escuche",
                fontSize = 10.sp,
                color = Color(0xFF1E88E5)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"$text\"",
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp
            )
        }
    }
}