package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.medix.presentation.ui.components.BottomNavigationBar



@Composable
fun NotificationsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("Notificaciones")
        }

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}