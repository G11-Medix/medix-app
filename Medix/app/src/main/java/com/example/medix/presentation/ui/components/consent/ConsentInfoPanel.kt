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
            .padding(20.dp)
            .fillMaxWidth()
            .semantics {
                contentDescription = "Información del documento legal"
            }
    ) {

        Text(
            text = "Consentimiento informado",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { heading() }
        )

        Spacer(Modifier.height(12.dp))

        state.document?.let {
            Text(
                text = "Versión: ${it.version}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Fecha: ${it.fechaPublicacion}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}