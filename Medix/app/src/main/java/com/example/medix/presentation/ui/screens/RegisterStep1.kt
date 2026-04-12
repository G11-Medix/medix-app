package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.register.CustomTextField
import com.example.medix.presentation.ui.components.register.DatePickerField
import com.example.medix.presentation.ui.components.register.DocumentTypeDropdown
import com.example.medix.presentation.viewmodels.auth.AuthViewModel

@Composable
fun RegisterStep1(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
    ){
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text("Registro de paciente")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = 0.33f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Text("Información personal")

        Spacer(Modifier.height(16.dp))

        CustomTextField(
            value = form.nombres,
            label = "Nombres"
        ) { newValue ->
            viewModel.updatePacienteForm { current ->
                current.copy(nombres = newValue)
            }
        }

        Spacer(Modifier.height(12.dp))

        CustomTextField(
            value = form.apellidos,
            label = "Apellidos"
        ) { newValue ->
            viewModel.updatePacienteForm { current ->
                current.copy(apellidos = newValue)
            }
        }

        Spacer(Modifier.height(12.dp))

        DocumentTypeDropdown(
            selected = form.tipoDocumento
        ) { tipo ->
            viewModel.updatePacienteForm {
                it.copy(tipoDocumento = tipo)
            }
        }

        Spacer(Modifier.height(12.dp))

        CustomTextField(
            value = form.numeroDocumento,
            label = "Número de Documento"
        ) { newValue ->
            viewModel.updatePacienteForm { current ->
                current.copy(numeroDocumento = newValue)
            }
        }

        Spacer(Modifier.height(12.dp))

        DatePickerField(
            selectedDate = form.fechaNacimiento
        ) { date ->
            viewModel.updatePacienteForm {
                it.copy(fechaNacimiento = date)
            }
        }

        state.errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            Text(it)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }
    }
}