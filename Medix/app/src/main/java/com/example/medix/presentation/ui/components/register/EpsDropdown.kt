package com.example.medix.presentation.ui.components.register

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.DropdownMenuItem

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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.medix.data.dto.EpsDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpsDropdown(
    selected: String,
    options: List<EpsDto>,
    isLoading: Boolean,
    onSelected: (EpsDto) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (!isLoading && options.isNotEmpty()) {
                expanded = !expanded
            }
        }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("EPS") },
            placeholder = { Text("Selecciona una EPS") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .semantics {
                    contentDescription = if (isLoading) {
                        "Cargando lista de EPS"
                    } else {
                        "Selector de EPS"
                    }
                },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { eps ->
                DropdownMenuItem(
                    text = { Text(eps.nombre) },
                    onClick = {
                        onSelected(eps)
                        expanded = false
                    }
                )
            }
        }
    }
}