package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.medix.core.auth.SessionManager
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.SectionTitle
import com.example.medix.presentation.ui.components.profile.InfoCard
import com.example.medix.presentation.ui.components.profile.TopBarProfile
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.profile.ProfileViewModel
import com.example.medix.data.dto.UserProfileDto

@Composable
fun ProfileScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {

    val viewModel: ProfileViewModel = hiltViewModel()

    val state by viewModel.uiState.collectAsState()



    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        TopBarProfile(
            onLogout = {
                viewModel.logout()
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {

            when (state) {

                is UiState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    val error = state as UiState.Error

                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(text = error.message, color = Color.Red)

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = { viewModel.loadProfile() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                is UiState.Success<*> -> {

                    val profile = (state as UiState.Success<*>).data as? UserProfileDto

                    if (profile == null) return@Box

                    Column {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = "${profile.nombres} ${profile.apellidos}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "Paciente Medix",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle("Información Personal")

                        InfoCard(Icons.Default.Person, "Nombres", profile.nombres)
                        InfoCard(Icons.Default.Badge, "Apellidos", profile.apellidos)
                        InfoCard(Icons.Default.Fingerprint, "Documento", profile.documento)
                        InfoCard(Icons.Default.LocalHospital, "EPS", profile.eps)

                        Spacer(modifier = Modifier.height(12.dp))

                        SectionTitle("Contacto y Seguridad")

                        InfoCard(Icons.Default.Email, "Correo", profile.correo)
                        InfoCard(Icons.Default.Phone, "Telefono", profile.telefono)
                    }
                }
            }
        }

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}