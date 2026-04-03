package com.example.medix.presentation.ui.components.schedule

import androidx.compose.material3.Card
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun VoiceCard(
    onMicClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1565C0)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E88E5))
                    .clickable { onMicClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Start voice",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Empezar agendamiento cita",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Asistente Medix",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}