package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.medix.R
import com.example.medix.presentation.ui.components.login.LoginCard
import com.example.medix.presentation.viewmodels.auth.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo con imagen - Más clara
        Image(
            painter = painterResource(id = R.drawable.iniciarapp_medix),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = if (isLandscape) Alignment.CenterStart else Alignment.TopCenter,
            alpha = 0.85f
        )

        // Capa de oscurecimiento muy sutil para asegurar legibilidad sin ocultar la imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background.copy(
                        alpha = if (isLandscape) 0.05f else 0.12f
                    )
                )
        )

        // Contenido con scroll e imePadding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Bottom
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1.2f))
                    Box(modifier = Modifier.weight(1f)) {
                        LoginCard(
                            state = state,
                            viewModel = viewModel,
                            compactMode = true
                        )
                    }
                }
            } else {
                // Espaciado dinámico para empujar la tarjeta hacia abajo en portrait
                Spacer(modifier = Modifier.weight(1f))
                
                LoginCard(
                    state = state,
                    viewModel = viewModel,
                    maxWidth = 360.dp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
