package com.example.medix.presentation.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.presentation.ui.screens.*
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.viewmodels.chat.ChatViewModel
import com.example.medix.presentation.viewmodels.profile.ProfileViewModel
import com.example.medix.presentation.viewmodels.status.AuthNavigationTarget
import com.example.medix.presentation.viewmodels.voice.VoiceViewModel

private const val CONFIRMATION_PAYLOAD_KEY = "confirmation_payload"

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val navViewModel: NavViewModel = hiltViewModel()

    val sessionState by navViewModel.sessionState.collectAsState()

    if (sessionState.isLoading) {
        SplashScreen()
        return
    }

    val startDestination = when {
        !sessionState.isLoggedIn -> Screen.Login.route
        !sessionState.hasAcceptedConsent -> Screen.Consent.route
        else -> Screen.Schedule.route
    }

    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(authState.navigationTarget) {
        when (authState.navigationTarget) {

            AuthNavigationTarget.CONSENT -> {
                navController.navigateAndClear(Screen.Consent.route)
            }

            AuthNavigationTarget.SCHEDULE -> {
                navController.navigateAndClear(Screen.Schedule.route)
            }

            AuthNavigationTarget.REGISTER -> {
                navController.navigateSingleTop(Screen.Register1.route)
            }

            else -> Unit
        }

        authViewModel.onNavigationHandled()
    }

    key(sessionState.isLoggedIn) {

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            // =====================
            // AUTH
            // =====================

            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        //
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
                        navController.popBackStack()
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



            composable(Screen.Consent.route) {

                ConsentScreen(
                    onAccept = {
                        navController.navigateAndClear(Screen.Schedule.route)
                    },
                    onReject = {
                        authViewModel.resetAuthState()
                        navController.navigateAndClear(Screen.Login.route)
                    }
                )
            }

            composable(Screen.Register3.route) {
                RegisterStep3(
                    viewModel = authViewModel,
                    onRegistrationSuccess = {
                        navController.navigateAndClear(Screen.Login.route)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // =====================
            // MAIN
            // =====================

            composable(Screen.Schedule.route) {
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

            composable(Screen.Voice.route) {
                val viewModel: VoiceViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsState()

                LaunchedEffect(state.navigateToConfirmation, state.appointmentConfirmation) {
                    val confirmation = state.appointmentConfirmation
                    if (state.navigateToConfirmation && confirmation != null) {
                        navigateToConfirmation(navController, confirmation)
                        viewModel.onConfirmationNavigationHandled()
                    }
                }

                VoiceScreen(
                    viewModel = viewModel,
                    onEndCall = {
                        if (viewModel.uiState.value.completed == true) {
                            val confirmation = viewModel.uiState.value.appointmentConfirmation
                            if (confirmation != null) {
                                navigateToConfirmation(navController, confirmation)
                            } else {
                                navController.navigateAndClear(Screen.Schedule.route)
                            }
                        } else {
                            navController.navigateAndClear(Screen.Schedule.route)
                        }
                    }
                )
            }

            composable(Screen.Confirmation.route) {
                val payload = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<AppointmentConfirmationDto>(CONFIRMATION_PAYLOAD_KEY)

                ConfirmationScreen(
                    appointment = payload,
                    onDone = {
                        navController.navigateAndClear(Screen.Schedule.route)
                    }
                )
            }

            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    currentRoute = Screen.Notifications.route,
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            }

            composable(Screen.Records.route) {
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

            composable(Screen.Profile.route) {

                val profileViewModel: ProfileViewModel = hiltViewModel()

                ProfileScreen(
                    currentRoute = Screen.Profile.route,
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    },
                    onLogout = {
                        profileViewModel.logout()
                        authViewModel.resetAuthState()
                        navController.navigateAndClear(Screen.Login.route)
                    }
                )
            }

            composable(Screen.Chat.route) {
                val viewModel: ChatViewModel = hiltViewModel()
                val state by viewModel.uiState.collectAsState()

                LaunchedEffect(state.navigateToConfirmation, state.appointmentConfirmation) {
                    val confirmation = state.appointmentConfirmation
                    if (state.navigateToConfirmation && confirmation != null) {
                        navigateToConfirmation(navController, confirmation)
                        viewModel.onConfirmationNavigationHandled()
                    }
                }

                ChatScreen(
                    currentRoute = Screen.Chat.route,
                    viewModel = viewModel,
                    onNavigate = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            }
        }
    }
}

private fun navigateToConfirmation(
    navController: NavController,
    confirmation: AppointmentConfirmationDto,
) {
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.set(CONFIRMATION_PAYLOAD_KEY, confirmation)

    navController.navigateSingleTop(Screen.Confirmation.route)
}
