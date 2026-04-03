package com.example.medix.presentation.ui.components.voice
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )
    }
}