package com.example.medix.presentation.ui.components.confirmation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.core.utils.DateUtils
import com.example.medix.data.dto.AppointmentConfirmationDto

@Composable
fun AppointmentInfoCard(data: AppointmentConfirmationDto) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                data.doctorName,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                DateUtils.formatAppointmentDateConfirmation(data.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            data.appointmentId?.let { appointmentId ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ID cita: $appointmentId",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Estado: ${data.status}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Ubicación de la clínica"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(data.clinicName)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Dirección de la cita"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(data.address)
            }
        }
    }
}