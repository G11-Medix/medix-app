package com.example.medix.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.medix.data.repositories.NotificationRepositoryImpl
import com.example.medix.domain.entities.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import com.example.medix.R
import com.example.medix.data.sources.local.TokenDataStore
import com.example.medix.di.NotificationModule
import com.example.medix.domain.repositories.DeviceRepository
import com.example.medix.domain.repositories.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject lateinit var deviceRepository: DeviceRepository

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

        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.saveNotification(notification)
        }

        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val notification = NotificationCompat.Builder(this, "medix_channel")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.IO).launch {
            tokenDataStore.saveToken(token)
        }
    }
}