package com.example.medix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.medix.presentation.ui.navigation.NavGraph
import com.example.medix.presentation.ui.theme.MedixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedixTheme {
                NavGraph()
            }
        }
    }
}


