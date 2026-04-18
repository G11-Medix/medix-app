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
        reachedBottom = scrollState.value == scrollState.maxValue
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        // 📜 Documento
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .semantics {
                    contentDescription = "Contenido del documento legal"
                }
        ) {
            Text(
                text = state.document?.contenido?: "",
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        // ⚠️ MENSAJE WCAG (CRÍTICO)
        if (!reachedBottom) {
            Text(
                text = "Debes leer todo el documento antes de aceptar",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(8.dp))
        }

        // ☑️ Checkbox accesible
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = checked,
                    enabled = reachedBottom,
                    onValueChange = { checked = it }
                )
                .padding(8.dp)
                .semantics {
                    contentDescription =
                        if (reachedBottom)
                            "Aceptar términos y condiciones"
                        else
                            "Debes leer todo el documento antes de aceptar"
                },
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

        Spacer(Modifier.height(12.dp))

        // 🔘 Botones (48dp mínimo)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Rechazar")
            }

            Button(
                onClick = onAccept,
                enabled = checked && reachedBottom,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Aceptar")
            }
        }
    }
}