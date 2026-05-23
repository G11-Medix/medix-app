package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import com.example.medix.presentation.ui.components.register.StepProgressBar

@Composable
fun RegisterStep3(
    viewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm

    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp > 600

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

            Text("Confirmación", style = MaterialTheme.typography.titleLarge)

            StepProgressBar(currentStep = 3, totalSteps = 3)

            Spacer(Modifier.height(16.dp))

            Text("Documento: ${form.tipoDocumento} ${form.numeroDocumento}")
            Text("Nombre: ${form.nombres} ${form.apellidos}")
            Text("Fecha: ${form.fechaNacimiento}")
            Text("Teléfono: ${form.telefono}")
            Text("Correo: ${form.correo}")
            Text("EPS: ${form.epsNombre}")

            Spacer(Modifier.height(16.dp))

            if (state.otpSent) {
                OutlinedTextField(
                    value = state.otpCode,
                    onValueChange = viewModel::updateOtpCode,
                    label = { Text("Código OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Código OTP")
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.registerPaciente() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar registro")
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Atrás")
            }
        }
    }
}