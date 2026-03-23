package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun RegisterStep2(onNext: () -> Unit, onBack: () -> Unit) {

    var contact by remember { mutableStateOf("") }
    var selectedSex by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "")
        }

        Text("Crear Cuenta - Paso 2 de 3")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(progress = 0.66f, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Número de celular o correo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text("Sexo")

        Row {

            FilterChip(
                selected = selectedSex == "Hombre",
                onClick = { selectedSex = "Hombre" },
                label = { Text("Hombre") }
            )

            Spacer(Modifier.width(8.dp))

            FilterChip(
                selected = selectedSex == "Mujer",
                onClick = { selectedSex = "Mujer" },
                label = { Text("Mujer") }
            )

            Spacer(Modifier.width(8.dp))

            FilterChip(
                selected = selectedSex == "Otro",
                onClick = { selectedSex = "Otro" },
                label = { Text("Otro") }
            )
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Ciudad") },
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, "") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(24.dp))

        Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
            Text("Siguiente")
        }

        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Atrás")
        }
    }
}