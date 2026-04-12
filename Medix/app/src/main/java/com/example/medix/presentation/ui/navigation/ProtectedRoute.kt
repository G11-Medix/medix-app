package com.example.medix.presentation.ui.navigation


import androidx.compose.runtime.Composable

@Composable
fun ProtectedRoute(
    isLoggedIn: Boolean,
    content: @Composable () -> Unit
) {
    if (isLoggedIn) {
        content()
    }
}