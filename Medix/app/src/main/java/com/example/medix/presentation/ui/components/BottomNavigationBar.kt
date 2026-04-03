package com.example.medix.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "schedule",
            onClick = { onNavigate("schedule") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Agendar") }
        )

        NavigationBarItem(
            selected = currentRoute == "records",
            onClick = { onNavigate("records") },
            icon = { Icon(Icons.Default.History, contentDescription = null) },
            label = { Text("Registros") }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") }
        )

        NavigationBarItem(
            selected = currentRoute == "chat",
            onClick = { onNavigate("chat") },
            icon = { Icon(Icons.Default.Chat, contentDescription = null) },
            label = { Text("Chat") }
        )
    }
}