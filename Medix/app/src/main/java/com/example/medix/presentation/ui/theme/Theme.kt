package com.example.medix.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*


private val LightColors = lightColorScheme(
    primary = PrimaryBlue
)

@Composable
fun MedixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}