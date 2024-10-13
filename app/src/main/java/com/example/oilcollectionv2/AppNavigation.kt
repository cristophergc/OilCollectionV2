package com.example.oilcollectionv2

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("dashboard") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                navigateToLogin = { navController.navigate("login") }
            )
        }
        composable("update") {
            UserDetailsForm(
                onUpdateSuccess = { navController.navigate("dashboard") },
                onUpdateFailure = {}
            )
        }
        composable("dashboard") {
            Dashboard(
                onCollectionRequest = { navController.navigate("confirmation") },
                onLogoutSuccess = { navController.navigate("login") }
            )
        }
        composable("confirmation") {
            ConfirmationScreen(onOkButtonClick = { navController.navigate("dashboard") })
        }
    }
}
