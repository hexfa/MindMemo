package com.mindmemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.mindmemo.presentation.ui.navigation.AppNavigation
import com.mindmemo.presentation.ui.theme.AppTheme
import com.mindmemo.presentation.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState(initial = false).value

            Crossfade(
                targetState = isDarkTheme,
                animationSpec = tween(durationMillis = 600),
                label = "ThemeTransition"
            ) { darkTheme ->
                AppTheme(darkTheme = darkTheme) {
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
