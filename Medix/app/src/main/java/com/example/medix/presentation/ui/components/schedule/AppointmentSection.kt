package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.medix.core.utils.DateUtils
import com.example.medix.domain.entities.Appointment
import com.example.medix.presentation.ui.components.records.AppointmentCard

@Composable
fun AppointmentSection(
    appointments: List<Appointment>,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAppointmentClick: (Appointment) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = "Próximas citas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Text(
                text = "Ver todas",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (appointments.isEmpty()) {
            Text(
                text = "No tienes citas próximas",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(appointments) { appointment ->
                    AppointmentCard(
                        name = appointment.name,
                        specialty = appointment.specialty,
                        date = DateUtils.formatAppointmentDate(
                            appointment.date,
                            appointment.hour
                        ),
                        state = appointment.state,
                        logo_url = appointment.logo_url,
                        onClick = { onAppointmentClick(appointment) }
                    )
                }
            }
        }
    }
}