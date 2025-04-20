package com.mindmemo.presentation.ui.screen.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.mindmemo.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun fab_isDisplayed_and_navigatesToNoteScreen() {
        composeTestRule.onNodeWithContentDescription("Add Note").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        composeTestRule.onNodeWithText("Note Detail").assertIsDisplayed()
    }

    @Test
    fun toggleLayout_changesBetweenListAndGrid() {
        composeTestRule.onNodeWithContentDescription("Toggle Layout").performClick()
    }

    @Test
    fun toggleTheme_changesThemeMode() {
        composeTestRule.onNodeWithContentDescription("Toggle Theme").performClick()
    }

    @Test
    fun searchMode_toggle_and_searchTextEntry() {
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithTag("SearchTextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("Test")
        composeTestRule.onNodeWithText("Test Note Title").assertIsDisplayed()
    }

    @Test
    fun backButton_inSearchMode_closesSearch() {
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("Note")
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.onNodeWithTag("SearchTextField").assertDoesNotExist()
    }
}