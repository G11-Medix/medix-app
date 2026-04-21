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

            GreetingSection()

            Spacer(modifier = Modifier.height(16.dp))

            VoiceCard(onMicClick = onStartVoice)

            Spacer(modifier = Modifier.height(24.dp))

            ContentState(
                state = state,
                viewModel = viewModel,
                onNavigate = onNavigate,
                modifier = Modifier.weight(1f)
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