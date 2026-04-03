package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medix.core.utils.DateUtils
import com.example.medix.di.RepositoryModule
import androidx.compose.ui.Alignment

import com.example.medix.presentation.ui.components.records.AppointmentCard
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.HeaderSection
import com.example.medix.presentation.ui.components.records.PastAppointmentCard
import com.example.medix.presentation.ui.components.SectionTitle
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModelFactory


@Composable
fun RecordsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {

    val repository = remember { RepositoryModule.provideAppointmentRepository() }

    val viewModel: AppointmentViewModel = viewModel(
        factory = AppointmentViewModelFactory(repository)
    )

    val upcoming = viewModel.upcomingAppointments
    val past = viewModel.pastAppointments

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {

        HeaderSection(onNotificationsClick = onNotificationsClick)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mis registros",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {


            if (upcoming.isEmpty() && past.isEmpty()) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No tienes citas registradas",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        onNavigate("schedule")
                    }) {
                        Text("Agendar cita")
                    }
                }

            } else {


                SectionTitle("Próximas citas")

                if (upcoming.isEmpty()) {
                    Text(
                        text = "No tienes citas próximas",
                        color = Color.Gray
                    )
                } else {
                    upcoming.take(5).forEach {
                        AppointmentCard(
                            name = it.name,
                            specialty = it.specialty,
                            date = DateUtils.formatAppointmentDate(it.date)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                SectionTitle("Citas Pasadas")

                if (past.isEmpty()) {
                    Text(
                        text = "No tienes citas pasadas",
                        color = Color.Gray
                    )
                } else {
                    past.take(5).forEach {
                        PastAppointmentCard(
                            name = it.name,
                            specialty = it.specialty,
                            date = DateUtils.formatAppointmentDate(it.date)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}