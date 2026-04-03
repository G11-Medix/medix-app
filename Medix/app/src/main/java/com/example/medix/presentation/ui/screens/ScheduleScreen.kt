package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medix.di.RepositoryModule
import com.example.medix.presentation.ui.components.*
import com.example.medix.presentation.ui.components.HeaderSection
import com.example.medix.presentation.ui.components.schedule.AppointmentSection
import com.example.medix.presentation.ui.components.schedule.GreetingSection
import com.example.medix.presentation.ui.components.schedule.VoiceCard
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModelFactory


@Composable
fun ScheduleScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onStartVoice: () -> Unit
) {

    val repository = remember { RepositoryModule.provideAppointmentRepository() }

    val viewModel: AppointmentViewModel = viewModel(
        factory = AppointmentViewModelFactory(repository)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {

        HeaderSection(onNotificationsClick = onNotificationsClick)

        Spacer(modifier = Modifier.height(16.dp))

        GreetingSection()

        Spacer(modifier = Modifier.height(16.dp))

        VoiceCard(onMicClick = onStartVoice)

        Spacer(modifier = Modifier.height(30.dp))

        if (viewModel.isLoading) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            AppointmentSection(
                appointments = viewModel.upcomingAppointments.take(2),
                onSeeAllClick = {
                    onNavigate("records")
                }
            )

        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

