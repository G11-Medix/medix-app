package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.platform.LocalConfiguration

import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.ui.components.register.*

@Composable
fun RegisterStep1(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = config.screenWidthDp > 600

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = if (isTablet) 500.dp else Dp.Unspecified)
        ) {

            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

            Text("Registro de paciente", style = MaterialTheme.typography.titleLarge)

            StepProgressBar(currentStep = 1, totalSteps = 3)

            Spacer(Modifier.height(16.dp))

            Text("Información personal", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            CustomTextField(
                value = form.nombres,
                label = "Nombres",
                onChange = { newValue ->
                    viewModel.updatePacienteForm {
                        it.copy(nombres = newValue)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            CustomTextField(
                value = form.apellidos,
                label = "Apellidos",
                onChange = { newValue ->
                    viewModel.updatePacienteForm {
                        it.copy(apellidos = newValue)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            DocumentTypeDropdown(
                selected = form.tipoDocumento,
                onSelectedChange = { tipo ->
                    viewModel.updatePacienteForm {
                        it.copy(tipoDocumento = tipo)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            CustomTextField(
                value = form.numeroDocumento,
                label = "Número de documento",
                onChange = { newValue ->
                    viewModel.updatePacienteForm {
                        it.copy(numeroDocumento = newValue)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            DatePickerField(
                selectedDate = form.fechaNacimiento,
                onDateSelected = { date ->
                    viewModel.updatePacienteForm {
                        it.copy(fechaNacimiento = date)
                    }
                }
            )

            state.errorMessage?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Siguiente")
            }

            if (isLandscape) Spacer(Modifier.height(40.dp))
        }
    }
}