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
                onCreateAccount = {
                    navController.navigate("register1")
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
                onCreate = { navController.navigate("login") },
                onBack = { navController.popBackStack() }
            )
        }
    }
}