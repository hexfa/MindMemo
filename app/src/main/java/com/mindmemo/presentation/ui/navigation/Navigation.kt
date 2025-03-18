package com.mindmemo.presentation.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.mindmemo.presentation.ui.screen.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
//        composable("home") { HomeScreen() }
    }
}