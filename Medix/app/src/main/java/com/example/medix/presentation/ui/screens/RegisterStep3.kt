package com.example.medix.presentation.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.components.PasswordField

@Composable
fun RegisterStep3(onCreate: () -> Unit, onBack: () -> Unit) {

    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var accept1 by remember { mutableStateOf(false) }
    var accept2 by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "")
        }

        Text("Crear Cuenta - Paso 3 de 3")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Text("Establece tu seguridad")

        Spacer(Modifier.height(16.dp))

        PasswordField(password) { password = it }

        Spacer(Modifier.height(12.dp))

        PasswordField(confirm) { confirm = it }

        Spacer(Modifier.height(12.dp))

        Row {
            Checkbox(checked = accept1, onCheckedChange = { accept1 = it })
            Text("Acepto política de tratamiento de datos")
        }

        Row {
            Checkbox(checked = accept2, onCheckedChange = { accept2 = it })
            Text("Acepto términos y condiciones")
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onCreate, modifier = Modifier.fillMaxWidth()) {
            Text("Crear Cuenta")
        }

        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Atrás")
        }
    }


}