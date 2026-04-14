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
            text = "Hola de nuevo",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = "Buenos días",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}