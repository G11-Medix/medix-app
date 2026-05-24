package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.core.utils.DateUtils
import com.example.medix.domain.entities.Appointment
import com.example.medix.presentation.ui.components.records.formatState
import com.example.medix.presentation.ui.components.records.getStateColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailBottomSheet(
    appointment: Appointment,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Title and Status Badge
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Detalle de la cita",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                val stateColor = getStateColor(appointment.state)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = stateColor.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, stateColor.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(8.dp).background(stateColor, CircleShape))
                        Text(
                            text = formatState(appointment.state).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = stateColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    DetailRow(
                        icon = Icons.Default.MedicalServices,
                        label = "Especialidad",
                        value = appointment.specialty
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp), 
                        thickness = 1.dp, 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    
                    DetailRow(
                        icon = Icons.Default.Event,
                        label = "Fecha y hora",
                        value = DateUtils.formatAppointmentDate(appointment.date, appointment.hour)
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp), 
                        thickness = 1.dp, 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    
                    DetailRow(
                        icon = Icons.Default.Business,
                        label = "Institución",
                        value = appointment.name
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Recommendations or Observation
            val isCancelled = appointment.state.trim().lowercase() in setOf("cancelled", "canceled", "cancelada", "cancelado")
            
            Text(
                text = if (isCancelled) "Observación" else "Recomendaciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isCancelled) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f) 
                                     else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isCancelled) {
                        ObservationItem("Usuario canceló la cita")
                    } else {
                        RecommendationItem("Llegar 15 minutos antes de la hora citada para el registro.")
                        RecommendationItem("Presentar documento de identidad original en recepción.")
                        RecommendationItem("En caso de no poder asistir, por favor cancela con anticipación.")
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Entendido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun RecommendationItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text, 
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ObservationItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Cancel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text, 
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
