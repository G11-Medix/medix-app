package com.example.medix.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.medix.presentation.ui.navigation.Screen

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {

        NavigationBarItem(
            selected = currentRoute == Screen.Schedule.route,
            onClick = { onNavigate(Screen.Schedule.route) },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Agendar")
            },
            label = { Text("Agendar") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Records.route,
            onClick = { onNavigate(Screen.Records.route) },
            icon = {
                Icon(Icons.Default.History, contentDescription = "Registros")
            },
            label = { Text("Registros") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,
            onClick = { onNavigate(Screen.Profile.route) },
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Perfil")
            },
            label = { Text("Perfil") }
        )

        NavigationBarItem(
            selected = currentRoute == Screen.Chat.route,
            onClick = { onNavigate(Screen.Chat.route) },
            icon = {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")
            },
            label = { Text("Chat") }
        )
    }
}