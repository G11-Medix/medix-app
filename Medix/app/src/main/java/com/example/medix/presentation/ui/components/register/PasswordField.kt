package com.example.medix.presentation.ui.components.register

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(value: String, onChange: (String) -> Unit) {

    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text("Contraseña") },

        visualTransformation =
            if (visible) VisualTransformation.None
            else PasswordVisualTransformation(),

        trailingIcon = {

            val icon =
                if (visible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff

            IconButton(onClick = { visible = !visible }) {
                Icon(icon, "")
            }
        },

        modifier = Modifier.fillMaxWidth()
    )
}