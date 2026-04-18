package com.example.medix.presentation.ui.screens

import android.content.res.Configuration

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medix.presentation.ui.components.consent.ConsentContentPanel
import com.example.medix.presentation.ui.components.consent.ConsentInfoPanel
import com.example.medix.presentation.ui.components.register.StepProgressBar
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.auth.ConsentViewModel
import com.example.medix.presentation.viewmodels.schedule.AppointmentViewModel
import com.example.medix.presentation.viewmodels.status.ConsentUiState


@Composable
fun ConsentScreen(
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val viewModel: ConsentViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "Pantalla de consentimiento informado"
            }
    ) {

        when (val result = uiState) {

            is UiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        result.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is UiState.Success -> {



                val state = result.data

                LaunchedEffect(state.accepted) {
                    if (state.accepted) {
                        onAccept()
                    }
                }

                if (isLandscape) {
                    Row(modifier = Modifier.fillMaxSize()) {



                        ConsentInfoPanel(
                            state = state,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )

                        ConsentContentPanel(
                            state = state,
                            onAccept = {
                                viewModel.onAccept()
                                onAccept()
                            },
                            onReject = onReject,
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {


                        ConsentInfoPanel(state = state)

                        ConsentContentPanel(
                            state = state,
                            onAccept = {
                                viewModel.onAccept()
                                onAccept()
                            },
                            onReject = onReject,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}