package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.components.confirmation.AppointmentInfoCard
import com.example.medix.presentation.ui.components.confirmation.SuccessHeader
import com.example.medix.presentation.ui.components.confirmation.TopBarConfirmation
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.res.Configuration
import androidx.compose.runtime.collectAsState
import com.example.medix.presentation.ui.components.confirmation.MapSection
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.getValue

@Composable
fun ConfirmationScreen(
    onDone: () -> Unit
) {
    val viewModel: VoiceViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val appointment = uiState.appointmentConfirmation

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
                    Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Cargando confirmación" },
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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

                            Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.height(16.dp))

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
                    text = "Un email de confirmación se envió con los detalles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.semantics {
                        contentDescription = "Se envió un correo de confirmación"
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp) // 👈 tamaño táctil WCAG
                    .semantics {
                        contentDescription = "Finalizar y salir de la confirmación"
                    },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Finalizar",
                    fontSize = 16.sp // 👈 evita texto muy pequeño
                )
            }
        }
    }
}