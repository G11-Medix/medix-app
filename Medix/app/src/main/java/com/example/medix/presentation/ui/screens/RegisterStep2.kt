package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.register.CustomTextField
import com.example.medix.presentation.ui.components.register.EpsDropdown
import com.example.medix.presentation.viewmodels.auth.AuthViewModel

@Composable
fun RegisterStep2(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm

    LaunchedEffect(Unit) {
        viewModel.loadEpsIfNeeded()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text("Crear Cuenta - Paso 2 de 3")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = 0.66f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))


        CustomTextField(
            value = form.telefono,
            label = "Teléfono"
        ) { input ->
            val normalized = when {
                input.isBlank() -> ""
                input.startsWith("+") -> input
                else -> "+$input"
            }
            viewModel.updatePhone(normalized)
        }

        Spacer(Modifier.height(12.dp))


        CustomTextField(
            value = form.correo,
            label = "Correo"
        ) { newValue ->
            viewModel.updatePacienteForm { current ->
                current.copy(correo = newValue)
            }
        }

        Spacer(Modifier.height(20.dp))


        EpsDropdown(
            selected = form.epsNombre,
            options = state.epsOptions,
            isLoading = state.isLoadingEps
        ) { eps ->
            viewModel.updatePacienteForm { current ->
                current.copy(
                    idEps = eps.idEps.toString(),
                    epsNombre = eps.nombre
                )
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

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atrás")
        }
    }
}
