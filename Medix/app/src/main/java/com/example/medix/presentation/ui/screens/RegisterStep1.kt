package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.foundation.clickable


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RegisterStep1(onNext: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var document by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var tipoDocumento by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        IconButton(onClick = {}) {
            Icon(Icons.Default.ArrowBack, "")
        }

        Text("Crear Cuenta")

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(progress = 0.33f, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Text("Información Personal")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombres y Apellidos") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))



        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = tipoDocumento,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Documento") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("CC") },
                    onClick = {
                        tipoDocumento = "CC"
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("CE") },
                    onClick = {
                        tipoDocumento = "CE"
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("TI") },
                    onClick = {
                        tipoDocumento = "TI"
                        expanded = false
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = document,
            onValueChange = { document = it },
            label = { Text("Número de Documento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        BirthDateField()

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField() {

    var birth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {

                    datePickerState.selectedDateMillis?.let {
                        birth = java.text.SimpleDateFormat(
                            "dd/MM/yyyy"
                        ).format(java.util.Date(it))
                    }

                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value = birth,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha de Nacimiento") },
        trailingIcon = {
            Icon(Icons.Default.CalendarMonth, "")
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
    )
}