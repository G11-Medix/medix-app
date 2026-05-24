package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.medix.presentation.ui.components.*
import com.example.medix.presentation.ui.components.schedule.AppointmentSection
import com.example.medix.presentation.ui.components.schedule.GreetingSection
import com.example.medix.presentation.ui.components.schedule.VoiceCard
import com.example.medix.presentation.ui.components.schedule.AppointmentDetailBottomSheet
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel
import com.example.medix.domain.entities.Appointment
import com.example.medix.presentation.ui.components.schedule.ContentState
import com.example.medix.presentation.viewmodels.profile.ProfileViewModel
import com.example.medix.data.dto.UserProfileDto

@Composable
fun ScheduleScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onStartVoice: () -> Unit
) {
    val viewModel: AppointmentViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val profileState by profileViewModel.uiState.collectAsState()

    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    val userName = (profileState as? UiState.Success<UserProfileDto>)?.data?.nombres

    if (selectedAppointment != null) {
        AppointmentDetailBottomSheet(
            appointment = selectedAppointment!!,
            onDismiss = { selectedAppointment = null }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp)
                .widthIn(max = 600.dp)
        ) {

            HeaderSection(onNotificationsClick = onNotificationsClick)

            Spacer(modifier = Modifier.height(16.dp))

            GreetingSection(userName = userName)

            Spacer(modifier = Modifier.height(16.dp))

            var showConsent by remember { mutableStateOf(false) }

            VoiceCard(onMicClick = { showConsent = true })

            if (showConsent) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showConsent = false },
                    title = { Text(text = "Autorización para uso de datos", style = MaterialTheme.typography.titleMedium) },
                    text = {
                        Text(
                            text = "Al iniciar el asistente de voz autorizas el uso y tratamiento de datos necesarios para procesar tu solicitud. Puedes cancelar si no deseas continuar.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showConsent = false
                                onStartVoice()
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { showConsent = false },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Cancelar")
                        }
                    },
                    properties = androidx.compose.ui.window.DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = false
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ContentState(
                state = state,
                viewModel = viewModel,
                onNavigate = onNavigate,
                modifier = Modifier.weight(1f),
                onAppointmentClick = { selectedAppointment = it }
            )
        }

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}