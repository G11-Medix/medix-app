package com.example.medix.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {

        NavigationBarItem(
            selected = currentRoute == "schedule",
            onClick = { onNavigate("schedule") },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Agendar")
            },
            label = { Text("Agendar") }
        )

        NavigationBarItem(
            selected = currentRoute == "records",
            onClick = { onNavigate("records") },
            icon = {
                Icon(Icons.Default.History, contentDescription = "Registros")
            },
            label = { Text("Registros") }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Perfil")
            },
            label = { Text("Perfil") }
        )

        NavigationBarItem(
            selected = currentRoute == "chat",
            onClick = { onNavigate("chat") },
            icon = {
                Icon(Icons.Default.Chat, contentDescription = "Chat")
            },
            label = { Text("Chat") }
        )
    }
}