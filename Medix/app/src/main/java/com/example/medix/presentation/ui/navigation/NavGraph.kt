package com.example.medix.presentation.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medix.core.auth.AuthSessionState
import com.example.medix.presentation.ui.screens.*
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel


@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()


    val navViewModel: NavViewModel = hiltViewModel()
    val sessionState by navViewModel.sessionState.collectAsState(
        initial = AuthSessionState()
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val authRoutes = setOf(
        Screen.Login.route,
        Screen.Register1.route,
        Screen.Register2.route,
        Screen.Register3.route
    )

    val protectedRoutes = setOf(
        Screen.Schedule.route,
        Screen.Voice.route,
        Screen.Confirmation.route,
        Screen.Notifications.route,
        Screen.Records.route,
        Screen.Profile.route
    )


    LaunchedEffect(sessionState.isLoggedIn, currentRoute) {

        val route = currentRoute ?: return@LaunchedEffect

        when {
            !sessionState.isLoggedIn && route in protectedRoutes -> {
                navController.navigateAndClear(Screen.Login.route)
            }

            sessionState.isLoggedIn && route in authRoutes -> {
                navController.navigateAndClear(Screen.Schedule.route)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // 👈 evita flicker
    ) {

        // =====================
        // AUTH
        // =====================

        composable(Screen.Login.route) {

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigateAndClear(Screen.Schedule.route)
                },
                onNavigateToRegister = {
                    navController.navigateSingleTop(Screen.Register1.route)
                }
            )
        }

        composable(Screen.Register1.route) {


            RegisterStep1(
                viewModel = authViewModel,
                onNext = {
                    navController.navigateSingleTop(Screen.Register2.route)
                },
                onBack = {
                    authViewModel.resetPacienteForm()
                    navController.navigateAndClear(Screen.Login.route)
                }
            )
        }

        composable(Screen.Register2.route) {

            RegisterStep2(
                viewModel = authViewModel,
                onNext = {
                    navController.navigateSingleTop(Screen.Register3.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Register3.route) {

            RegisterStep3(
                viewModel = authViewModel,
                onRegistrationSuccess = {
                    navController.navigateAndClear(Screen.Schedule.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // =====================
        // MAIN (PROTECTED)
        // =====================

        composable(Screen.Schedule.route) {
            ProtectedRoute(sessionState.isLoggedIn) {
                ScheduleScreen(
                    currentRoute = Screen.Schedule.route,
                    onNotificationsClick = {
                        navController.navigateSingleTop(Screen.Notifications.route)
                    },
                    onStartVoice = {
                        navController.navigateSingleTop(Screen.Voice.route)
                    },
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            }
        }

        composable(Screen.Voice.route) {
            ProtectedRoute(sessionState.isLoggedIn) {

                val viewModel: VoiceViewModel = hiltViewModel()

                VoiceScreen(
                    viewModel = viewModel,
                    onEndCall = {
                        if (viewModel.uiState.value.completed == true) {
                            navController.navigateAndClear(Screen.Confirmation.route)
                        } else {
                            navController.navigateAndClear(Screen.Schedule.route)
                        }
                    }
                )
            }
        }

        composable(Screen.Confirmation.route) {
            ProtectedRoute(sessionState.isLoggedIn) {
                ConfirmationScreen(
                    onDone = {
                        navController.navigateAndClear(Screen.Schedule.route)
                    }
                )
            }
        }

        composable(Screen.Notifications.route) {
            ProtectedRoute(sessionState.isLoggedIn) {
                NotificationsScreen(
                    currentRoute = Screen.Notifications.route,
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            }
        }

        composable(Screen.Records.route) {
            ProtectedRoute(sessionState.isLoggedIn) {
                RecordsScreen(
                    currentRoute = Screen.Records.route,
                    onNotificationsClick = {
                        navController.navigateSingleTop(Screen.Notifications.route)
                    },
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            }
        }

        composable(Screen.Profile.route) {
            ProtectedRoute(sessionState.isLoggedIn) {
                ProfileScreen(
                    currentRoute = Screen.Profile.route,
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            }
        }
    }
}