package com.example.medix.presentation.ui.components.schedule


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun GreetingSection() {
    Column {
        Text(
            text = "HOLA DE NUEVO",
            color = Color(0xFF1E88E5),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Buenos días",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}