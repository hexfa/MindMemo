package com.mindmemo.presentation.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.mindmemo.presentation.ui.screen.HomeScreen
import com.mindmemo.presentation.ui.screen.NoteScreen
import com.mindmemo.presentation.ui.screen.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController = navController) }
        composable("note") { NoteScreen(navController = navController) }
    }
}