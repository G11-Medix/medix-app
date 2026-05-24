package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.LocalTime

@Composable
fun GreetingSection(userName: String? = null) {

    val greeting = getGreetingByTime()
    val fullGreeting = if (!userName.isNullOrBlank()) "$greeting, $userName!" else greeting

    Column {
        Text(
            text = "Hola de nuevo",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = fullGreeting,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun getGreetingByTime(): String {
    val hour = LocalTime.now().hour

    return when (hour) {
        in 5..11,58 -> "Buenos días"
        in 12..17 -> "Buenas tardes"
        in 18..23,59 -> "Buenas noches"
        else -> "Buenas madrugadas"
    }
}
