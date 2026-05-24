package com.example.medix.presentation.ui.components.confirmation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medix.data.dto.AppointmentConfirmationDto

@Composable
fun MapSection(
    data: AppointmentConfirmationDto,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Ubicación",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .semantics {
                    contentDescription = "Mapa con la ubicación de la cita en ${data.address}"
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                OpenStreetMapView(
                    lat = data.lat,
                    lon = data.lon
                )
            }
        }
    }
}
