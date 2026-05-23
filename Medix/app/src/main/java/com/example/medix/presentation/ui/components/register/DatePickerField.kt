package com.example.medix.presentation.ui.components.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    val formatter = remember {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha de nacimiento") },
        placeholder = { Text("Selecciona una fecha") },

        // 🔥 ESTO ES LO IMPORTANTE PARA WCAG
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Campo de fecha de nacimiento"
            },

        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Abrir calendario"
                )
            }
        },

        // 🔥 ESTO HACE QUE TODO EL CAMPO SEA CLICKABLE
        interactionSource = remember { MutableInteractionSource() }
            .also { source ->
                LaunchedEffect(source) {
                    source.interactions.collect {
                        showDialog = true
                    }
                }
            }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        onDateSelected(date.format(formatter))
                    }
                    showDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}