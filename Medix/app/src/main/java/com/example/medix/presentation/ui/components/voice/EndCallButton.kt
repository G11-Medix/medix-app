package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EndCallButton(
    onEndCall: () -> Unit
) {
    Button(
        onClick = onEndCall,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFC62828)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("Terminar llamada", color = Color.White)
    }
}