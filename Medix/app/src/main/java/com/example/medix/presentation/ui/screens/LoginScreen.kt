package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.theme.PrimaryBlue
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.viewmodels.status.AuthNavigationTarget

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.navigationTarget) {
        when (state.navigationTarget) {
            AuthNavigationTarget.SCHEDULE -> {
                viewModel.onNavigationHandled()
                onLoginSuccess()
            }
            AuthNavigationTarget.REGISTER -> {
                viewModel.onNavigationHandled()
                onNavigateToRegister()
            }
            AuthNavigationTarget.NONE -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Medix",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.height(60.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Autenticacion por SMS",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ingresa el telefono autorizado del paciente. Solo los usuarios habilitados pueden recibir codigo OTP.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.phone,
                    onValueChange = viewModel::updatePhone,
                    label = { Text("Teléfono") },
                    placeholder = { Text("+573001234567") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null)
                    },
                )

                if (state.otpSent) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.otpCode,
                        onValueChange = viewModel::updateOtpCode,
                        label = { Text("Código OTP") },
                        placeholder = { Text("123456") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                    )

                    TextButton(
                        onClick = viewModel::resendLoginOtp,
                        enabled = !state.isLoading,
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text("Reenviar código")
                    }
                }

                state.infoMessage?.let { info ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = info,
                        color = PrimaryBlue,
                        fontSize = 13.sp,
                    )
                }

                state.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
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
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !state.isLoading,
                ) {
                    Text(
                        text = if (state.isLoading) {
                            "Procesando..."
                        } else if (state.otpSent) {
                            "Verificar código"
                        } else {
                            "Enviar código"
                        }
                    )
                }
            }
        }
    }
}
