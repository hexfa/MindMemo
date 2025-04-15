package com.mindmemo.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mindmemo.presentation.ui.screen.HomeScreen
import com.mindmemo.presentation.ui.screen.NoteScreen
import com.mindmemo.presentation.ui.screen.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SPLASH) {
        composable(SPLASH) {
            SplashScreen(navController)
        }

        composable(HOME) {
            HomeScreen(navController = navController)
        }

        composable(
            route = "$NOTE?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val noteIdArg = backStackEntry.arguments?.getInt("noteId")
            val noteId = if (noteIdArg == -1) null else noteIdArg

            NoteScreen(navController = navController, noteId = noteId)
        }
    }
}