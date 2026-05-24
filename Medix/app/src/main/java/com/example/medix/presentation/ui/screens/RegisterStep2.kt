package com.example.medix.presentation.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.components.common.PhoneNumberInput
import com.example.medix.presentation.ui.components.register.CustomTextField
import com.example.medix.presentation.ui.components.register.EpsDropdown
import com.example.medix.presentation.ui.components.register.StepProgressBar
import com.example.medix.presentation.viewmodels.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
                StepProgressBar(currentStep = 2, totalSteps = 3)

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
                            text = "Contacto y EPS",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Text(
                            text = "Dinos cómo contactarte y a qué entidad de salud perteneces.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(8.dp))

                        PhoneNumberInput(
                            countryCode = state.phoneCountryCode,
                            phoneNumber = form.telefono,
                            onCountryCodeChange = viewModel::updatePhoneCountryCode,
                            onPhoneNumberChange = viewModel::updatePhone,
                            label = "Teléfono de contacto",
                        )

                        CustomTextField(
                            value = form.correo,
                            label = "Correo electrónico(opcional)",
                            onChange = { newValue ->
                                viewModel.updatePacienteForm { it.copy(correo = newValue) }
                            }
                        )

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

                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Atrás", fontSize = 16.sp)
                        }
                    }
                }
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
