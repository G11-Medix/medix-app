package com.example.medix.presentation.ui.components.records
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun PastAppointmentCard(
    name: String,
    specialty: String,
    date: String,
    state: String,
    logo_url: String,
) {
    val visualState = getVisualState(state, isPast = true)
    val isCancelled = visualState.lowercase() in setOf("cancelled", "canceled", "cancelada", "cancelado")
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCancelled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = if (isCancelled) CardDefaults.cardElevation(defaultElevation = 0.dp) else CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            AsyncImage(
                model = logo_url,
                contentDescription = "Logo",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    specialty,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        Icons.Default.History,
                        contentDescription = "Cita pasada"
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val visualState = getVisualState(state, isPast = true)
            val stateColor = getStateColor(visualState)
            
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isCancelled) stateColor.copy(alpha = 0.12f) else stateColor.copy(alpha = 0.7f)
            ) {
                Text(
                    text = formatState(visualState),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCancelled) stateColor else Color.White
                )
            }
        }
    }
}