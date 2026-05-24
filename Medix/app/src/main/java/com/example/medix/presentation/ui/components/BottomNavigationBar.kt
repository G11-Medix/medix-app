package com.example.medix.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medix.presentation.ui.navigation.Screen

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // WCAG 2.2 AA compliant colors
        val selectedBg = MaterialTheme.colorScheme.primary // PrimaryBlue #1E6FD9
        val selectedFg = MaterialTheme.colorScheme.primary // Use primary blue for text (visible on light surface)
        val selectedIconFg = Color.White // White icon on blue background
        val unselectedIcon = Color(0xFF424242) // TextSecondary - Dark gray
        val unselectedText = Color(0xFF424242) // TextSecondary - Dark gray

        // Home / Inicio
        NavigationBarItem(
            selected = currentRoute == Screen.Schedule.route,
            onClick = { onNavigate(Screen.Schedule.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconFg,
                selectedTextColor = selectedFg,
                indicatorColor = selectedBg,
                unselectedIconColor = unselectedIcon,
                unselectedTextColor = unselectedText,
            ),
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Inicio")
            },
            label = {
                Text(
                    "Inicio",
                    fontWeight = if (currentRoute == Screen.Schedule.route) FontWeight.Bold else FontWeight.Normal
                )
            }
        )

        // Records / Registros
        NavigationBarItem(
            selected = currentRoute == Screen.Records.route,
            onClick = { onNavigate(Screen.Records.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconFg,
                selectedTextColor = selectedFg,
                indicatorColor = selectedBg,
                unselectedIconColor = unselectedIcon,
                unselectedTextColor = unselectedText,
            ),
            icon = {
                Icon(Icons.Default.History, contentDescription = "Registros")
            },
            label = {
                Text(
                    "Registros",
                    fontWeight = if (currentRoute == Screen.Records.route) FontWeight.Bold else FontWeight.Normal
                )
            }
        )

        // Chat
        NavigationBarItem(
            selected = currentRoute == Screen.Chat.route,
            onClick = { onNavigate(Screen.Chat.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconFg,
                selectedTextColor = selectedFg,
                indicatorColor = selectedBg,
                unselectedIconColor = unselectedIcon,
                unselectedTextColor = unselectedText,
            ),
            icon = {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat")
            },
            label = {
                Text(
                    "Chat",
                    fontWeight = if (currentRoute == Screen.Chat.route) FontWeight.Bold else FontWeight.Normal
                )
            }
        )

        // Profile / Perfil
        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,
            onClick = { onNavigate(Screen.Profile.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedIconFg,
                selectedTextColor = selectedFg,
                indicatorColor = selectedBg,
                unselectedIconColor = unselectedIcon,
                unselectedTextColor = unselectedText,
            ),
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Perfil")
            },
            label = {
                Text(
                    "Perfil",
                    fontWeight = if (currentRoute == Screen.Profile.route) FontWeight.Bold else FontWeight.Normal
                )
            }
        )
    }
}