package com.example.medix.presentation.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import com.example.medix.presentation.viewmodels.notification.SharedNotificationViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HeaderSection(
    onNotificationsClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Medix",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        // Use SharedNotificationViewModel to show badge on header bell
        val sharedVm: SharedNotificationViewModel = hiltViewModel()
        val notificationCount by sharedVm.notificationCount.collectAsState()

        // Refresh count when header composes (e.g., when entering the screen)
        LaunchedEffect(Unit) {
            sharedVm.updateNotificationCount()
        }

        IconButton(onClick = onNotificationsClick) {
            Box {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones"
                )

                                if (notificationCount > 0) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .offset(x = 6.dp, y = (-6).dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 2.dp)
                                            )
                                        }
                                    }
                                }
            }
        }

                        // Register broadcast receiver to update count immediately when notification arrives
                        val context = LocalContext.current
                        DisposableEffect(Unit) {
                            val receiver = object : BroadcastReceiver() {
                                override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
                                    sharedVm.updateNotificationCount()
                                }
                            }

                            val filter = IntentFilter("com.example.medix.ACTION_NOTIFICATION_RECEIVED")
                            ContextCompat.registerReceiver(
                                context,
                                receiver,
                                filter,
                                ContextCompat.RECEIVER_NOT_EXPORTED
                            )

                            onDispose {
                                try {
                                    context.unregisterReceiver(receiver)
                                } catch (_: Exception) {
                                }
                            }
                        }
    }
}