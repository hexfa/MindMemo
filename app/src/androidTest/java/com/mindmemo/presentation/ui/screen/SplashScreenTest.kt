package com.mindmemo.presentation.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.mindmemo.presentation.ui.navigation.HOME
import com.mindmemo.presentation.ui.navigation.SPLASH
import com.mindmemo.presentation.ui.theme.AppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        navController = TestNavHostController(composeTestRule.activity)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        navController.setCurrentDestination(SPLASH)
    }

    @Test
    fun splashScreen_displaysNoteIcon() {
        composeTestRule.setContent {
            AppTheme {
                SplashScreen(navController = navController)
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Note")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun splashScreen_navigatesToHome_afterDelay() {
        composeTestRule.setContent {
            AppTheme {
                SplashScreen(navController = navController)
            }
        }

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            navController.currentDestination?.route == HOME
        }

        assert(navController.currentDestination?.route == HOME)
    }
}