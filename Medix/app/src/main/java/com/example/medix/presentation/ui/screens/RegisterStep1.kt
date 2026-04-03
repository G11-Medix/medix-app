package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.auth.AuthViewModel

@Composable
fun RegisterStep1(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text("Registro de paciente")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(progress = 0.33f, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Text("Información personal")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = form.nombres,
            onValueChange = {
                viewModel.updatePacienteForm { current -> current.copy(nombres = it) }
            },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = form.apellidos,
            onValueChange = {
                viewModel.updatePacienteForm { current -> current.copy(apellidos = it) }
            },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = form.tipoDocumento,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de Documento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar tipo de documento")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("CC", "TI", "CE", "PA").forEach { tipo ->
                DropdownMenuItem(
                    text = { Text(tipo) },
                    onClick = {
                        viewModel.updatePacienteForm { current ->
                            current.copy(tipoDocumento = tipo)
                        }
                        expanded = false
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = form.numeroDocumento,
            onValueChange = {
                viewModel.updatePacienteForm { current -> current.copy(numeroDocumento = it) }
            },
            label = { Text("Número de Documento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = form.fechaNacimiento,
            onValueChange = {
                viewModel.updatePacienteForm { current -> current.copy(fechaNacimiento = it) }
            },
            label = { Text("Fecha de Nacimiento") },
            placeholder = { Text("YYYY-MM-DD") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        state.errorMessage?.let { error ->
            Spacer(Modifier.height(12.dp))
            Text(error)
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
