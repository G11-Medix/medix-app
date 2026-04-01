package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.*
import com.example.medix.presentation.ui.components.HeaderSection
import com.example.medix.presentation.ui.components.schedule.AppointmentSection
import com.example.medix.presentation.ui.components.schedule.GreetingSection
import com.example.medix.presentation.ui.components.schedule.VoiceCard

@Composable
fun ScheduleScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onStartVoice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {

        HeaderSection(
            onNotificationsClick = onNotificationsClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        GreetingSection()

        Spacer(modifier = Modifier.height(16.dp))

        VoiceCard(
            onMicClick = onStartVoice
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppointmentSection()

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}