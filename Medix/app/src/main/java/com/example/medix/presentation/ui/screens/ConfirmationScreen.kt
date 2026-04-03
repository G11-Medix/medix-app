package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.medix.presentation.ui.components.confirmation.AppointmentInfoCard
import com.example.medix.presentation.ui.components.confirmation.OpenStreetMapView
import com.example.medix.presentation.ui.components.confirmation.SuccessHeader
import com.example.medix.presentation.ui.components.confirmation.TopBarConfirmation
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.confirmation.ConfirmationViewModel
import com.example.medix.presentation.viewmodels.confirmation.ConfirmationViewModelFactory

@Composable
fun ConfirmationScreen(
    onDone: () -> Unit
) {

    val viewModel: ConfirmationViewModel = viewModel(
        factory = ConfirmationViewModelFactory()
    )

    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {


        TopBarConfirmation()


        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {

            when (state) {


                is UiState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }


                is UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = state.message,
                            color = Color.Red
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = { viewModel.loadData() }) {
                            Text("Reintentar")
                        }
                    }
                }


                is UiState.Success -> {

                    val data = state.data

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        SuccessHeader(
                            title = data.title,
                            message = data.message,
                            status = data.status
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        AppointmentInfoCard(data)

                        Spacer(modifier = Modifier.height(16.dp))


                        Card(
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Box(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                OpenStreetMapView(
                                    lat = data.lat,
                                    lon = data.lon
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Un email de confirmación se envió con los detalles",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }


        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Finalizar")
        }
    }
}