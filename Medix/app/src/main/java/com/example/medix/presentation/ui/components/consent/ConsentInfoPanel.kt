package com.example.medix.presentation.ui.components.consent

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.viewmodels.status.ConsentUiState


@Composable
fun ConsentInfoPanel(
    state: ConsentUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .semantics {
                contentDescription = "Información del documento legal"
            }
    ) {

        Text(
            text = "Consentimiento informado",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics { heading() }
        )

        Spacer(Modifier.height(8.dp))

        state.document?.let {
            Text("Versión: ${it.version}")
            Text("Fecha: ${it.fechaPublicacion}")
        }
    }
}