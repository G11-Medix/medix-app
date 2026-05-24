package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medix.domain.entities.Notification
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.notification.NotificationCard
import com.example.medix.presentation.viewmodels.notification.NotificationViewModel
import com.example.medix.presentation.viewmodels.notification.SharedNotificationViewModel

@Composable
fun NotificationsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val sharedVm: SharedNotificationViewModel = hiltViewModel()

    // Clear badge when entering screen
    LaunchedEffect(Unit) {
        sharedVm.clearBadge()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp)
                .widthIn(max = 600.dp)
        ) {

            Text(
                "Notificaciones",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.weight(1f)) {

                when (state) {

                    is UiState.Loading -> {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Error -> {
                        val error = state as UiState.Error

                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                Text(
                                    text = error.message,
                                    color = Color.Red
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(onClick = { viewModel.loadNotifications() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }

                    is UiState.Success -> {
                        val success = state as UiState.Success<List<Notification>>
                        val notifications = success.data

                        if (notifications.isEmpty()) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay notificaciones")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(notifications) { notification ->
                                    NotificationCard(notification)
                                }
                            }
                        }
                    }
                }
            }
        }

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}