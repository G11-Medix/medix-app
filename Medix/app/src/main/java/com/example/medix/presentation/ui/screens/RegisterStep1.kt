package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.ui.components.register.*

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Paciente", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StepProgressBar(currentStep = 1, totalSteps = 3)

                Spacer(Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 500.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información Personal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Text(
                            text = "Ingresa tus datos básicos para comenzar el registro.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(8.dp))

                        CustomTextField(
                            value = form.nombres,
                            label = "Nombres",
                            onChange = { newValue ->
                                viewModel.updatePacienteForm { it.copy(nombres = newValue) }
                            }
                        )

                        CustomTextField(
                            value = form.apellidos,
                            label = "Apellidos",
                            onChange = { newValue ->
                                viewModel.updatePacienteForm { it.copy(apellidos = newValue) }
                            }
                        )

                        DocumentTypeDropdown(
                            selected = form.tipoDocumento,
                            onSelectedChange = { tipo ->
                                viewModel.updatePacienteForm { it.copy(tipoDocumento = tipo) }
                            }
                        )

                        CustomTextField(
                            value = form.numeroDocumento,
                            label = "Número de documento",
                            onChange = { newValue ->
                                viewModel.updatePacienteForm { it.copy(numeroDocumento = newValue) }
                            }
                        )

                        DatePickerField(
                            selectedDate = form.fechaNacimiento,
                            onDateSelected = { date ->
                                viewModel.updatePacienteForm { it.copy(fechaNacimiento = date) }
                            }
                        )

                        state.errorMessage?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = onNext,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Siguiente", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
