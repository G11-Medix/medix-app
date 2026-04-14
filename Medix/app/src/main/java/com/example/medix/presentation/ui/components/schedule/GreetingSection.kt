package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.LocalTime

@Composable
fun GreetingSection() {

    val greeting = getGreetingByTime()

    Column {
        Text(
            text = "Hola de nuevo",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = greeting,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

private fun getGreetingByTime(): String {
    val hour = LocalTime.now().hour

    return when (hour) {
        in 5..11 -> "Buenos días"
        in 12..17 -> "Buenas tardes"
        in 18..22 -> "Buenas noches"
        else -> "Buenas madrugadas"
    }
}