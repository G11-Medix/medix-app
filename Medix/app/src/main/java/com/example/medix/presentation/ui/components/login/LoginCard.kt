package com.example.medix.presentation.ui.components.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.components.common.PhoneNumberInput
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.viewmodels.status.AuthUiState

@Composable
fun LoginCard(
    state: AuthUiState,
    viewModel: AuthViewModel,
    maxWidth: Dp = 360.dp,
    compactMode: Boolean = false,
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = maxWidth),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (compactMode) 16.dp else 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de WhatsApp destacado - Más compacto
            Box(
                modifier = Modifier
                    .size(if (compactMode) 48.dp else 60.dp)
                    .background(Color(0xFF25D366), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(if (compactMode) 28.dp else 36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Autenticación WhatsApp",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa el número de teléfono del paciente para continuar.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            PhoneNumberInput(
                countryCode = state.phoneCountryCode,
                phoneNumber = state.phone,
                onCountryCodeChange = viewModel::updatePhoneCountryCode,
                onPhoneNumberChange = viewModel::updatePhone,
                label = "Teléfono",
            )

            if (state.otpSent) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.otpCode,
                    onValueChange = viewModel::updateOtpCode,
                    label = { Text("Código de verificación") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    singleLine = true
                )
            }

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Verifica los datos e intenta de nuevo.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (state.infoMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.infoMessage,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (state.otpSent) {
                        viewModel.verifyOtpAndResolveFlow()
                    } else {
                        viewModel.requestOtp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (state.otpSent) "Verificar" else "Continuar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
