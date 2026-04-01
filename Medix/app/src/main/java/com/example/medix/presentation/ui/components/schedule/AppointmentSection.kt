package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.medix.presentation.ui.components.records.AppointmentCard

@Composable
fun AppointmentSection() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Próximas Citas",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ver todas",
            color = Color(0xFF1E88E5)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    AppointmentCard(
        name = "Dr. Sarah Jenkins",
        specialty = "Cardiologist • Video Call",
        date = "Mañana, 10:30 AM"
    )

    Spacer(modifier = Modifier.height(8.dp))

    AppointmentCard(
        name = "Dr. Michael Chen",
        specialty = "General Practitioner • Clinic",
        date = "Viernes, 2:15 PM"
    )
}