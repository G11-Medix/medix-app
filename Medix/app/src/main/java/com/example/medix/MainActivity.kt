package com.example.medix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.medix.core.auth.SessionManager
import com.example.medix.presentation.ui.navigation.NavGraph
import com.example.medix.presentation.ui.theme.MedixTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osm", MODE_PRIVATE)
        )
        SessionManager.initialize(applicationContext)

        setContent {
            MedixTheme {
                NavGraph()
            }
        }
    }
}


