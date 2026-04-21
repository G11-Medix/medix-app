package com.example.medix.presentation.ui.components.confirmation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape



import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

import androidx.compose.ui.unit.dp

import com.example.medix.data.dto.AppointmentConfirmationDto

@Composable
fun MapSection(
    data: AppointmentConfirmationDto,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp, max = 320.dp)
            .semantics {
                contentDescription = "Mapa con la ubicación de la cita en ${data.address}"
            }
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            OpenStreetMapView(
                lat = data.lat,
                lon = data.lon
            )
        }
    }
}