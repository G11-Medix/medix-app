package com.example.medix.presentation.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.components.PasswordField
import com.example.medix.presentation.ui.theme.PrimaryBlue
@Composable
fun LoginScreen(
    onCreateAccount: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    var documento by remember { mutableStateOf("") }
    var remember by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, "")
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Medix",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.weight(1f))
        }

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
                    Icons.Default.MedicalServices,
                    contentDescription = "",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Bienvenido",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Entra a tu propia cuenta para acceder a tu registro de citas",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = documento,
                    onValueChange = { documento = it },
                    label = { Text("Número de Documento") },
                    placeholder = { Text("ej. 0000000000") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField(confirm) { confirm = it }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Checkbox(
                        checked = remember,
                        onCheckedChange = { remember = it }
                    )

                    Text("Recordarme")

                    Spacer(modifier = Modifier.weight(1f))

                    ClickableText(
                        text = AnnotatedString("¿Olvidaste tu contraseña?"),
                        onClick = {}
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onLoginSuccess, // <- navega a "schedule"
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Ingresar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("¿Eres nuevo?")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onCreateAccount,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Crear Cuenta")
                }
            }
        }
    }
}