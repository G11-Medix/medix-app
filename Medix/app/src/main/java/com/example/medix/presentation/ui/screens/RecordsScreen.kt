package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.HeaderSection
import com.example.medix.presentation.ui.components.records.PastAppointmentCard
import com.example.medix.presentation.ui.components.SectionTitle
import com.example.medix.presentation.ui.components.records.EmptyState
import com.example.medix.presentation.ui.components.records.RecordsColumn
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel
@Composable
fun RecordsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {

    val viewModel: AppointmentViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()


    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

//    LaunchedEffect(Unit) {
//        viewModel.loadAppointments()
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp) // espacio navbar
        ) {

            HeaderSection(onNotificationsClick = onNotificationsClick)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mis registros",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {

                is UiState.Loading -> {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    val error = state as UiState.Error

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = error.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.loadAppointments() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                is UiState.Success -> {

                    val data =
                        (state as UiState.Success<List<com.example.medix.domain.entities.Appointment>>).data

                    val upcoming = viewModel.upcomingAppointments
                    val past = viewModel.pastAppointments
                    val cancelled = viewModel.cancelledAppointments

                    if (data.isEmpty()) {

                        EmptyState(onNavigate)

                    } else {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            if (isLandscape) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {

                                    RecordsColumn(
                                        title = "Próximas citas",
                                        emptyText = "No tienes citas próximas",
                                        items = upcoming,
                                        isPast = false,
                                        modifier = Modifier.weight(1f)
                                    )

                                    RecordsColumn(
                                        title = "Citas pasadas",
                                        emptyText = "No tienes citas pasadas",
                                        items = past,
                                        isPast = true,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                RecordsColumn(
                                    title = "Citas canceladas",
                                    emptyText = "No tienes citas canceladas",
                                    items = cancelled,
                                    isPast = true
                                )

                            } else {

                                RecordsColumn(
                                    title = "Próximas citas",
                                    emptyText = "No tienes citas próximas",
                                    items = upcoming,
                                    isPast = false
                                )

                                RecordsColumn(
                                    title = "Citas pasadas",
                                    emptyText = "No tienes citas pasadas",
                                    items = past,
                                    isPast = true
                                )

                                RecordsColumn(
                                    title = "Citas canceladas",
                                    emptyText = "No tienes citas canceladas",
                                    items = cancelled,
                                    isPast = true
                                )
                            }
                        }
                    }
                }
            }
        }

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}