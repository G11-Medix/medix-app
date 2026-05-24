package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.presentation.ui.components.confirmation.*
import android.content.res.Configuration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics

fun getAuthorizationMessage(especialidad: String?): String? {
    if (especialidad == null) return null

    return when (especialidad.lowercase()) {
        "medicina general" -> null
        "pediatría", "pediatria" -> "Pediatría generalmente no requiere autorización previa de la EPS."
        else -> "Esta especialidad puede requerir autorización previa de la EPS antes de la atención."
    }
}

@Composable
fun ConfirmationScreen(
    appointment: AppointmentConfirmationDto?,
    onDone: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val authMessage = appointment?.let {
        getAuthorizationMessage(it.title)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .semantics { contentDescription = "Pantalla de confirmación de cita" },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            TopBarConfirmation()

            Spacer(modifier = Modifier.height(20.dp))

            if (appointment == null) {
                Box(
                    Modifier.fillMaxWidth().padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontró la confirmación de la cita.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                if (isLandscape) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1.2f).semantics { heading() }
                        ) {
                            SuccessHeader(
                                title = appointment.title,
                                message = appointment.message,
                                status = appointment.status
                            )

                            authMessage?.let { message ->
                                Spacer(modifier = Modifier.height(16.dp))
                                MedicalWarningCard(message)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            AppointmentInfoCard(appointment)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            MapSection(appointment)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            RecommendationsSection()
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SuccessHeader(
                            title = appointment.title,
                            message = appointment.message,
                            status = appointment.status
                        )

                        authMessage?.let { message ->
                            Spacer(modifier = Modifier.height(16.dp))
                            MedicalWarningCard(message)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        AppointmentInfoCard(appointment)

                        Spacer(modifier = Modifier.height(24.dp))

                        MapSection(appointment)

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        RecommendationsSection()
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Finalizar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MedicalWarningCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9C4) // Soft yellow
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFBC02D), // Darker yellow/amber
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5D4037), // Dark brown for readability
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RecommendationsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Recomendaciones",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
        )
        
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                BulletPoint("Llegar 15 minutos antes de la hora citada.")
                BulletPoint("Llevar documento de identidad original.")
                BulletPoint("Presentar orden médica física si la tiene.")
            }
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("•", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Bold)
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
