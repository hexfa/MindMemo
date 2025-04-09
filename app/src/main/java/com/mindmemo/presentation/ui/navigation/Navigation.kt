package com.mindmemo.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.mindmemo.presentation.ui.screen.HomeScreen
import com.mindmemo.presentation.ui.screen.NoteScreen
import com.mindmemo.presentation.ui.screen.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable(
            route = "note?noteId={noteId}",
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