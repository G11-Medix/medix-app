package com.example.medix.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.medix.presentation.ui.screens.*

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onCreateAccount = { navController.navigate("register1") },
                onLoginSuccess = {
                    navController.navigate("schedule") {
                        popUpTo("login") { inclusive = true } // elimina login del backstack
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("register1") {
            RegisterStep1(
                onNext = { navController.navigate("register2") }
            )
        }

        composable("register2") {
            RegisterStep2(
                onNext = { navController.navigate("register3") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("register3") {
            RegisterStep3(
                onCreate = {
                    navController.navigate("schedule") {
                        popUpTo("schedule") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }


        composable("schedule") {
            ScheduleScreen(
                currentRoute = "schedule",
                onNotificationsClick = { navController.navigate("notifications") },
                onStartVoice = { navController.navigate("voice") },
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("schedule")
                        launchSingleTop = true
                    }
                }
            )
        }


        composable("voice") {
            VoiceScreen(
                onEndCall = {
                    navController.navigate("confirmation")
                }
            )
        }


        composable("confirmation") {
            ConfirmationScreen(
                onDone = {
                    navController.navigate("schedule") {
                        popUpTo("schedule") { inclusive = true }
                    }
                }
            )
        }


        composable("notifications") {
            NotificationsScreen(
                currentRoute = "notifications",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("notifications")
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("records") {
            RecordsScreen(
                currentRoute = "records",
                onNotificationsClick = { navController.navigate("notifications") },
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("records")
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                currentRoute = "profile",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("profile")
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}