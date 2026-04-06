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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.viewmodels.status.AuthNavigationTarget

@Composable
fun RegisterStep3(
    viewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm

    LaunchedEffect(state.navigationTarget) {
        if (state.navigationTarget == AuthNavigationTarget.SCHEDULE) {
            viewModel.onNavigationHandled()
            onRegistrationSuccess()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text("Crear Cuenta - Paso 3 de 3")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Text("Confirma los datos del paciente")

        Spacer(Modifier.height(16.dp))

        Text("Documento: ${form.tipoDocumento} ${form.numeroDocumento}")
        Spacer(Modifier.height(8.dp))
        Text("Paciente: ${form.nombres} ${form.apellidos}")
        Spacer(Modifier.height(8.dp))
        Text("Fecha de nacimiento: ${form.fechaNacimiento}")
        Spacer(Modifier.height(8.dp))
        Text("Teléfono: ${form.telefono}")
        Spacer(Modifier.height(8.dp))
        Text("Correo: ${form.correo.ifBlank { "Sin correo" }}")
        Spacer(Modifier.height(8.dp))
        Text("Estado: ACTIVO")
        Spacer(Modifier.height(8.dp))
        Text("EPS: ${form.epsNombre.ifBlank { "Sin seleccionar" }}")

        if (state.otpSent) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.otpCode,
                onValueChange = viewModel::updateOtpCode,
                label = { Text("Código OTP") },
                placeholder = { Text("123456") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
            )

            TextButton(
                onClick = viewModel::resendRegisterOtp,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Reenviar código")
            }
        }

        state.errorMessage?.let { error ->
            Spacer(Modifier.height(12.dp))
            Text(error)
        }

        state.infoMessage?.let { info ->
            Spacer(Modifier.height(12.dp))
            Text(info)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.registerPaciente() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
        ) {
            Text(
                when {
                    state.isLoading && state.otpSent -> "Verificando..."
                    state.isLoading -> "Enviando código..."
                    state.otpSent -> "Verificar y Crear Cuenta"
                    else -> "Enviar código"
                }
            )
        }

        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Atrás")
        }
    }
}
