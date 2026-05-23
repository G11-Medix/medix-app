package com.example.medix

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.medix.presentation.ui.navigation.NavGraph
import com.example.medix.presentation.ui.theme.MedixTheme
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osm", MODE_PRIVATE)
        )

        val initialRoute = intent?.getStringExtra("route")

        setContent {
            MedixTheme {
                NavGraph(initialRoute = initialRoute)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "medix_channel",
                "Medix Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "Canal de notificaciones"

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }
    }
}