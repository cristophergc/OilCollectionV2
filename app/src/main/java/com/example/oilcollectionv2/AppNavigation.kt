package com.example.oilcollectionv2

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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

            val registerViewModel: RegisterViewModel = viewModel()

            RegisterScreen(
                navigateToLogin = { navController.navigate("login") },
                navigateToUserDetailsForm = { navController.navigate("userDetailsForm") },
                viewModel = registerViewModel
            )
        }
        composable("userDetailsForm") {
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
