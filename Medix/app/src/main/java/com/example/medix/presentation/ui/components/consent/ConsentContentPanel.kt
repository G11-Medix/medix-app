package com.example.medix.presentation.ui.components.consent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.viewmodels.status.ConsentUiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun ConsentContentPanel(
    state: ConsentUiState,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    var checked by remember { mutableStateOf(false) }
    var reachedBottom by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.value) {
        reachedBottom = scrollState.maxValue > 0 &&
                scrollState.value >= scrollState.maxValue
    }

    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {

        // 📜 DOCUMENTO
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // 🔵 PROGRESO
                LinearProgressIndicator(
                    progress = {
                        if (scrollState.maxValue == 0) 0f
                        else scrollState.value.toFloat() / scrollState.maxValue.toFloat()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 📜 SCROLL REAL
                Box(
                    modifier = Modifier
                        .weight(1f) // 🔥 CLAVE: fuerza altura real
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Text(
                        text = state.document?.contenido ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (!reachedBottom) {
            Text(
                text = "Debes leer todo el documento antes de aceptar",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = checked,
                    enabled = reachedBottom,
                    onValueChange = { checked = it }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = checked,
                onCheckedChange = null,
                enabled = reachedBottom
            )

            Spacer(Modifier.width(8.dp))

            Text("He leído y acepto los términos")
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Rechazar")
            }

            Button(
                onClick = onAccept,
                enabled = checked && reachedBottom,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Aceptar")
            }
        }
    }
}