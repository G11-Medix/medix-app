package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.ui.components.login.LoginCard

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit, // 👈 ya no se usa
    onNavigateToRegister: () -> Unit, // 👈 ya no se usa
) {
    val state by viewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        if (isLandscape) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // IZQUIERDA (branding)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Medix",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Autenticación segura vía SMS",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                LoginCard(state, viewModel)
            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 500.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                        Text(
                            text = "Medix",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bienvenid@! Inicie sesión o registrese con su teléfono",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                LoginCard(state, viewModel)
            }
        }
    }
}