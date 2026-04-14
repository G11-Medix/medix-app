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
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel
import com.example.medix.domain.entities.Appointment
import com.example.medix.presentation.ui.components.schedule.ContentState

@Composable
fun ScheduleScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onStartVoice: () -> Unit
) {
    val viewModel: AppointmentViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp) // espacio navbar
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
        ) {

            HeaderSection(onNotificationsClick = onNotificationsClick)

            Spacer(modifier = Modifier.height(16.dp))

            if (isLandscape) {
                Row(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.weight(1f)) {
                        GreetingSection()
                        Spacer(modifier = Modifier.height(16.dp))
                        VoiceCard(onMicClick = onStartVoice)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        ContentState(state, viewModel, onNavigate)
                    }
                }
            } else {
                GreetingSection()

                Spacer(modifier = Modifier.height(16.dp))

                VoiceCard(onMicClick = onStartVoice)

                Spacer(modifier = Modifier.height(24.dp))

                ContentState(state, viewModel, onNavigate)
            }
        }

        // NAVBAR FIJO
        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}