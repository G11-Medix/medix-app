package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.common.PhoneNumberInput
import com.example.medix.presentation.ui.components.register.CustomTextField
import com.example.medix.presentation.ui.components.register.EpsDropdown
import com.example.medix.presentation.ui.components.register.StepProgressBar
import com.example.medix.presentation.viewmodels.auth.AuthViewModel

@Composable
fun RegisterStep2(
    viewModel: AuthViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val form = state.pacienteForm

    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp > 600

    LaunchedEffect(Unit) {
        viewModel.loadEpsIfNeeded()
    }

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

            Text("Paso 2 de 3", style = MaterialTheme.typography.titleLarge)

            StepProgressBar(currentStep = 2, totalSteps = 3)

            Spacer(Modifier.height(16.dp))

            PhoneNumberInput(
                countryCode = state.phoneCountryCode,
                phoneNumber = form.telefono,
                onCountryCodeChange = viewModel::updatePhoneCountryCode,
                onPhoneNumberChange = viewModel::updatePhone,
                label = "Teléfono",
            )

            Spacer(Modifier.height(12.dp))

            CustomTextField(
                value = form.correo,
                label = "Correo",
                onChange = { newValue ->
                    viewModel.updatePacienteForm {
                        it.copy(correo = newValue)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            EpsDropdown(
                selected = form.epsNombre,
                options = state.epsOptions,
                isLoading = state.isLoadingEps,
                onSelected = { eps ->
                    viewModel.updatePacienteForm {
                        it.copy(
                            idEps = eps.idEps.toString(),
                            epsNombre = eps.nombre
                        )
                    }
                }
            )

            Spacer(Modifier.height(24.dp))

            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                Text("Siguiente")
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Atrás")
            }
        }
    }
}