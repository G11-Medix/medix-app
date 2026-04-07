package com.example.medix.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medix.core.auth.SessionManager
import com.example.medix.core.network.SupabaseProvider
import com.example.medix.data.repositories.AuthRepositoryImpl
import com.example.medix.di.RepositoryModule
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
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel
import com.example.medix.presentation.viewmodels.voice.VoiceViewModelFactory
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionState by SessionManager.sessionState.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            authRepository = AuthRepositoryImpl(SupabaseProvider.createClientOrNull()),
            pacienteRepository = RepositoryModule.providePacienteRepository(),
        )
    )

    LaunchedEffect(sessionState.isLoggedIn, currentRoute) {
        val route = currentRoute ?: return@LaunchedEffect
        val authRoutes = setOf("login", "register1", "register2", "register3")
        val protectedRoutes = setOf("schedule", "voice", "confirmation", "notifications", "records", "profile")

        when {
            !sessionState.isLoggedIn && route in protectedRoutes -> {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
            sessionState.isLoggedIn && route in authRoutes -> {
                navController.navigate("schedule") {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (sessionState.isLoggedIn) "schedule" else "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("schedule") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register1") {
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
            if (sessionState.isLoggedIn) {
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
        }

        composable("voice") {
            if (sessionState.isLoggedIn) {
                val repository = remember { RepositoryModule.provideVoiceRepository() }

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
                        if (viewModel.uiState.value.completed == true) {
                            navController.navigate("confirmation") {
                                popUpTo("confirmation") { inclusive = true }
                            }
                        } else {
                            navController.navigate("schedule") {
                                popUpTo("schedule") { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
        composable("confirmation") {
            if (sessionState.isLoggedIn) {
                ConfirmationScreen(
                    onDone = {
                        navController.navigate("schedule") {
                            popUpTo("schedule") { inclusive = true }
                        }
                    }
                )
            }
        }

        composable("notifications") {
            if (sessionState.isLoggedIn) {
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
        }

        composable("records") {
            if (sessionState.isLoggedIn) {
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
        }

        composable("profile") {
            if (sessionState.isLoggedIn) {
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
}
