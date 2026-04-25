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
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.common.PhoneNumberInput
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import androidx.compose.material3.minimumInteractiveComponentSize
import com.example.medix.presentation.viewmodels.status.AuthUiState

@Composable
fun LoginCard(
    state: AuthUiState,
    viewModel: AuthViewModel
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 400.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Autenticación por teléfono",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Autenticación por SMS",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ingresa el teléfono autorizado del paciente.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            PhoneNumberInput(
                countryCode = state.phoneCountryCode,
                phoneNumber = state.phone,
                onCountryCodeChange = viewModel::updatePhoneCountryCode,
                onPhoneNumberChange = viewModel::updatePhone,
                label = "Teléfono",
            )

            if (state.otpSent) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.otpCode,
                    onValueChange = viewModel::updateOtpCode,
                    label = { Text("Código OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            state.errorMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ocurrió un error inesperado. Por favor, inténtalo de nuevo.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.infoMessage?.let { info ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

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