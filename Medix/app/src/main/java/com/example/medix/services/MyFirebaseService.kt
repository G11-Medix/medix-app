package com.example.medix.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.medix.domain.entities.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import com.example.medix.R
import com.example.medix.MainActivity
import com.example.medix.data.sources.local.TokenDataStore
import com.example.medix.domain.repositories.NotificationRepository
import com.example.medix.core.auth.SessionManager
import com.example.medix.presentation.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var tokenDataStore: TokenDataStore

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.notification?.title ?: "Medix"
        val body = remoteMessage.notification?.body ?: ""

        val notification = Notification(
            title = title,
            body = body,
            timestamp = System.currentTimeMillis()
        )

        // Save notification once and notify UI
        CoroutineScope(Dispatchers.IO).launch {
            val pacienteId = sessionManager.getPacienteId()
            notificationRepository.saveNotification(notification, pacienteId)
            try {
                val intent = Intent("com.example.medix.ACTION_NOTIFICATION_RECEIVED")
                intent.setPackage(packageName)
                sendBroadcast(intent)
            } catch (_: Exception) {
                // ignore
            }
        }

        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        // Crear intent para abrir la app en la pantalla de notificaciones
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("route", Screen.Notifications.route)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "medix_channel")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onNewToken(token: String) {
        runBlocking(Dispatchers.IO) { tokenDataStore.saveToken(token) }
    }
}