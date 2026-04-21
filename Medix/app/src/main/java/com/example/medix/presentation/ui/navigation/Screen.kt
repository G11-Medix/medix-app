package com.example.medix.presentation.ui.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register1 : Screen("register1")
    object Register2 : Screen("register2")
    object Register3 : Screen("register3")
    object Schedule : Screen("schedule")
    object Voice : Screen("voice")
    object Confirmation : Screen("confirmation")
    object Notifications : Screen("notifications")
    object Records : Screen("records")
    object Profile : Screen("profile")

    object Consent : Screen("consent")

}