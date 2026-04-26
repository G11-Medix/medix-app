package com.example.medix.presentation.ui.components.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.common.PhoneNumberInput
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import androidx.compose.material3.minimumInteractiveComponentSize
import com.example.medix.presentation.viewmodels.status.AuthUiState

@Composable
fun LoginCard(
    state: AuthUiState,
    viewModel: AuthViewModel,
    maxWidth: Dp = 400.dp,
    compactMode: Boolean = false,
) {
    val cardCorner = if (compactMode) 24.dp else 28.dp
    val contentPadding = if (compactMode) 16.dp else 24.dp
    val phoneIconContainerCorner = if (compactMode) 16.dp else 20.dp
    val phoneIconSize = if (compactMode) 52.dp else 64.dp
    val phoneIconPadding = if (compactMode) 10.dp else 14.dp
    val topSpacing = if (compactMode) 12.dp else 20.dp
    val subtitleSpacing = if (compactMode) 6.dp else 8.dp
    val formSpacing = if (compactMode) 16.dp else 24.dp
    val otpSpacing = if (compactMode) 10.dp else 16.dp
    val messageSpacing = if (compactMode) 8.dp else 12.dp
    val infoSpacing = if (compactMode) 6.dp else 10.dp
    val buttonTopSpacing = if (compactMode) 14.dp else 20.dp

    Card(
        shape = RoundedCornerShape(cardCorner),
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = maxWidth),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                shape = RoundedCornerShape(phoneIconContainerCorner),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Autenticación por teléfono",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(phoneIconSize)
                        .padding(phoneIconPadding)
                )
            }

            Spacer(modifier = Modifier.height(topSpacing))

            Text(
                text = "Autenticación por SMS",
                style = if (compactMode) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(subtitleSpacing))

            Text(
                text = "Ingresa el teléfono autorizado del paciente.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(formSpacing))

            PhoneNumberInput(
                countryCode = state.phoneCountryCode,
                phoneNumber = state.phone,
                onCountryCodeChange = viewModel::updatePhoneCountryCode,
                onPhoneNumberChange = viewModel::updatePhone,
                label = "Teléfono",
            )

            if (state.otpSent) {
                Spacer(modifier = Modifier.height(otpSpacing))

                OutlinedTextField(
                    value = state.otpCode,
                    onValueChange = viewModel::updateOtpCode,
                    label = { Text("Código OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            state.errorMessage?.let {
                Spacer(modifier = Modifier.height(messageSpacing))

                Text(
                    text = "Ocurrió un error inesperado. Por favor, inténtalo de nuevo.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.infoMessage?.let { info ->
                Spacer(modifier = Modifier.height(infoSpacing))
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(buttonTopSpacing))

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
                    .minimumInteractiveComponentSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text("Continuar")
                }
            }
        }
    }
}