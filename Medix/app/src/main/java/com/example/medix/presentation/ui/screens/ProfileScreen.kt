package com.example.medix.presentation.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medix.core.auth.SessionManager
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.SectionTitle
import com.example.medix.presentation.ui.components.profile.InfoCard
import com.example.medix.presentation.ui.components.profile.InfoCardWithButton
import com.example.medix.presentation.ui.components.profile.InfoCardWithStatus
import com.example.medix.presentation.ui.components.profile.TopBarProfile
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.profile.ProfileViewModel
import com.example.medix.presentation.viewmodels.profile.ProfileViewModelFactory

@Composable
fun ProfileScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {

    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory()
    )

    val state = viewModel.uiState
    val onLogout = { SessionManager.clearSession() }

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

        TopBarProfile(onLogout = onLogout)


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
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(text = state.message, color = Color.Red)

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = { viewModel.loadProfile() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }


                is UiState.Success -> {

                    val profile = state.data

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

                        InfoCardWithStatus(
                            icon = Icons.Default.Email,
                            title = "Correo",
                            value = profile.correo,
                            status = if (profile.correoVerificado) "Verified" else "Not verified"
                        )

                        InfoCardWithButton(
                            icon = Icons.Default.Phone,
                            title = "Telefono",
                            value = profile.telefono,
                            buttonText = if (profile.telefonoVerificado) "Verified" else "Verify"
                        )
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
