package com.example.medix.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medix.BuildConfig

import com.example.medix.core.network.PacienteApiService
import com.example.medix.core.network.SupabaseProvider

import com.example.medix.data.repositories.AuthRepositoryImpl
import com.example.medix.data.repositories.PacienteRepositoryImpl

import com.example.medix.di.RepositoryModule
import com.example.medix.domain.repositories.PacienteRepository

import com.example.medix.presentation.ui.screens.ConfirmationScreen
import com.example.medix.presentation.ui.screens.LoginScreen
import com.example.medix.presentation.ui.screens.NotificationsScreen
import com.example.medix.presentation.ui.screens.ProfileScreen
import com.example.medix.presentation.ui.screens.RecordsScreen
import com.example.medix.presentation.ui.screens.RegisterStep1
import com.example.medix.presentation.ui.screens.RegisterStep2
import com.example.medix.presentation.ui.screens.RegisterStep3
import com.example.medix.presentation.ui.screens.ScheduleScreen
import com.example.medix.presentation.ui.screens.VoiceScreen
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.viewmodels.auth.AuthViewModelFactory
import com.example.medix.presentation.viewmodels.status.ConversationStatus
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel
import com.example.medix.presentation.viewmodels.voice.VoiceViewModelFactory
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            authRepository = AuthRepositoryImpl(SupabaseProvider.createClientOrNull()),
            pacienteRepository = RepositoryModule.providePacienteRepository(),
        )
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
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
                viewModel = authViewModel,
                onNext = { navController.navigate("register2") },
            )
        }

        composable("register2") {
            RegisterStep2(
                viewModel = authViewModel,
                onNext = { navController.navigate("register3") },
                onBack = { navController.popBackStack() },
            )
        }

        composable("register3") {
            RegisterStep3(
                viewModel = authViewModel,
                onRegistrationSuccess = {
                    navController.navigate("schedule") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() },
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



