package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.medix.presentation.ui.components.confirmation.AppointmentInfoCard
import com.example.medix.presentation.ui.components.confirmation.OpenStreetMapView
import com.example.medix.presentation.ui.components.confirmation.SuccessHeader
import com.example.medix.presentation.ui.components.confirmation.TopBarConfirmation
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.confirmation.ConfirmationViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.res.Configuration
import com.example.medix.presentation.ui.components.confirmation.MapSection

@Composable
fun ConfirmationScreen(
    onDone: () -> Unit
) {
    val viewModel: ConfirmationViewModel = hiltViewModel()
    val state = viewModel.uiState

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.loadData() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                is UiState.Success -> {
                    val data = state.data

                    if (isLandscape) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                SuccessHeader(
                                    title = data.title,
                                    message = data.message,
                                    status = data.status
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                AppointmentInfoCard(data)
                            }

                            MapSection(data)

                        }

                    } else {

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

                            MapSection(data)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Un email de confirmación se envió con los detalles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Finalizar")
            }
        }
    }
}