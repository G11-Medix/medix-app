package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        else -> "⚠️ Esta especialidad puede requerir autorización previa de la EPS antes de la atención."
    }
}

@Composable
fun ConfirmationScreen(
    appointment: AppointmentConfirmationDto?,
    onDone: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val authMessage = appointment?.let {
        getAuthorizationMessage(it.title) // ideal: reemplazar por specialty real
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
                .padding(16.dp)
        ) {

            TopBarConfirmation()

            Spacer(modifier = Modifier.height(16.dp))

            if (appointment == null) {

                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontró la confirmación de la cita.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

            } else {

                if (isLandscape) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .semantics { heading() }
                        ) {

                            SuccessHeader(
                                title = appointment.title,
                                message = appointment.message,
                                status = appointment.status
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // 📌 MENSAJE CONDICIONAL
                            authMessage?.let { message ->
                                Text(
                                    text = message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .fillMaxWidth()
                                        .semantics {
                                            contentDescription = "Mensaje de autorización EPS"
                                        }
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            AppointmentInfoCard(appointment)
                        }

                        MapSection(
                            appointment,
                            modifier = Modifier.semantics {
                                contentDescription = "Ubicación de la cita en el mapa"
                            }
                        )
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // 📌 MENSAJE CONDICIONAL
                        authMessage?.let { message ->
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth()
                                    .semantics {
                                        contentDescription = "Mensaje de autorización EPS"
                                    }
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        AppointmentInfoCard(appointment)

                        Spacer(modifier = Modifier.height(16.dp))

                        MapSection(
                            appointment,
                            modifier = Modifier.semantics {
                                contentDescription = "Ubicación de la cita en el mapa"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Recomendaciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Finalizar", fontSize = 16.sp)
            }
        }
    }
}