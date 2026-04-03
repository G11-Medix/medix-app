package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.medix.core.utils.DateUtils
import com.example.medix.domain.entities.Appointment
import com.example.medix.presentation.ui.components.records.AppointmentCard

@Composable
fun AppointmentSection(
    appointments: List<Appointment>,
    onSeeAllClick: () -> Unit
) {

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
            color = Color(0xFF1E88E5),
            modifier = Modifier.clickable {
                onSeeAllClick()
            }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (appointments.isEmpty()) {
        Text(
            text = "No tienes citas próximas",
            color = Color.Gray
        )
    } else {
        appointments.forEach { appointment ->
            AppointmentCard(
                name = appointment.name,
                specialty = appointment.specialty,
                date = DateUtils.formatAppointmentDate(appointment.date)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}