package com.example.medix.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.medix.di.RepositoryModule

import com.example.medix.presentation.ui.screens.*
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel
import com.example.medix.presentation.viewmodels.voice.VoiceViewModelFactory



import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder



import com.example.medix.presentation.viewmodels.status.ConversationStatus

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onCreateAccount = { navController.navigate("register1") },
                onLoginSuccess = {
                    navController.navigate("schedule") {
                        popUpTo("login") { inclusive = true }
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

            val repository = remember { RepositoryModule.provideVoiceRepository()}

            val viewModel: VoiceViewModel = viewModel(
                factory = VoiceViewModelFactory(
                    repository = repository,
                    recorder = AudioRecorder(context),
                    player = AudioPlayer(context),
                    context = context
                )
            )

            VoiceScreen(
                viewModel = viewModel,
                onEndCall = {
                    if (viewModel.uiState.value.status == ConversationStatus.ERROR) {
                        navController.navigate("schedule") {
                            popUpTo("schedule") { inclusive = true }
                        }
                    } else {
                        navController.navigate("confirmation")
                    }
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





