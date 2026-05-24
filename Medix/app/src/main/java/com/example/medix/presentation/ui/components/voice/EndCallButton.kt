package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.theme.ErrorRed

@Composable
fun EndCallButton(
    onEndCall: () -> Unit
) {
    Button(
        onClick = onEndCall,
        colors = ButtonDefaults.buttonColors(
            containerColor = ErrorRed // ✅ ACCESIBILIDAD: Usar color accesible (5.1:1 de contraste)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            // ✅ ACCESIBILIDAD: Agregar semántica para claridad adicional
            .semantics {
                role = Role.Button
                contentDescription = "Terminar llamada"
            }
    ) {
        Text("Terminar llamada", color = Color.White)
    }
}