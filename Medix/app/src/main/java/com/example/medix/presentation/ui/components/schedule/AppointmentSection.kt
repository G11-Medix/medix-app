package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
            text = "Próximas citas",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Ver todas",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onSeeAllClick() }
                .semantics { contentDescription = "Ver todas las citas" }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (appointments.isEmpty()) {
        Text(
            text = "No tienes citas próximas",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        Column {
            appointments.forEach {
                AppointmentCard(
                    name = it.name,
                    specialty = it.specialty,
                    date = DateUtils.formatAppointmentDate(it.date)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}