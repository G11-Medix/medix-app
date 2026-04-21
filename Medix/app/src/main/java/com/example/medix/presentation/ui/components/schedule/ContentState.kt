package com.example.medix.presentation.ui.components.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.medix.domain.entities.Appointment
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel

@Composable
fun ContentState(
    state: UiState<*>,
    viewModel: AppointmentViewModel,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {

        is UiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { viewModel.loadAppointments() }) {
                    Text("Reintentar")
                }
            }
        }

        is UiState.Success<*> -> {
            val success = state as UiState.Success<List<Appointment>>

            val filteredAppointments = success.data
                .filter { it.state.lowercase() != "cancelled" }
                .take(3)

            AppointmentSection(
                appointments = filteredAppointments,
                onSeeAllClick = { onNavigate("records") }
            )
        }
    }
}