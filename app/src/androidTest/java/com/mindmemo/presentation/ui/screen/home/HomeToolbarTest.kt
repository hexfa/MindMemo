package com.mindmemo.presentation.ui.screen.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import androidx.compose.runtime.setValue

class HomeToolbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleNotes_whenNotInSearchMode() {
        composeTestRule.setContent {
            HomeToolbarTestWrapper()
        }
        composeTestRule.onNodeWithText("Notes").assertIsDisplayed()
    }

    @Test
    fun displaysSearchTextField_whenSearchClicked() {
        var isSearchMode by mutableStateOf(false)
        composeTestRule.setContent {
            HomeToolbarTestWrapper(
                isSearchMode = isSearchMode,
                onSearchClick = { isSearchMode = true }
            )
        }
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNode(hasSetTextAction()).assertIsDisplayed()
    }

    @Test
    fun searchTextInput_and_clearButton_works() {
        var searchText by mutableStateOf("Test")
        var isSearchMode by mutableStateOf(true)

        composeTestRule.setContent {
            HomeToolbarTestWrapper(
                isSearchMode = isSearchMode,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchClick = {}
            )
        }

        composeTestRule.onNodeWithText("Test").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Clear Search").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Test").assertDoesNotExist()
    }

    @Test
    fun dropdownMenu_showsOptions_whenMoreClicked() {
        composeTestRule.setContent {
            HomeToolbarTestWrapper()
        }

        composeTestRule.onNodeWithContentDescription("More").performClick()

        composeTestRule.onNodeWithText("Toggle Layout").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toggle Theme").assertIsDisplayed()
    }

    @Test
    fun toggleLayout_callback_invoked() {
        var toggled = false
        composeTestRule.setContent {
            HomeToolbarTestWrapper(onToggleLayout = { toggled = true })
        }

        composeTestRule.onNodeWithContentDescription("More").performClick()
        composeTestRule.onNodeWithText("Toggle Layout").performClick()
        composeTestRule.runOnIdle {
            assert(toggled)
        }
    }

    @Test
    fun toggleTheme_callback_invoked() {
        var toggled = false
        composeTestRule.setContent {
            HomeToolbarTestWrapper(onToggleTheme = { toggled = true })
        }

        composeTestRule.onNodeWithContentDescription("More").performClick()
        composeTestRule.onNodeWithText("Toggle Theme").performClick()
        composeTestRule.runOnIdle {
            assert(toggled)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeToolbarTestWrapper(
    isGrid: Boolean = false,
    isDarkTheme: Boolean = false,
    isSearchMode: Boolean = false,
    searchText: String = "",
    onSearchTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onToggleLayout: () -> Unit = {},
    onToggleTheme: () -> Unit = {}
) {
    HomeToolbar(
        isGrid = isGrid,
        isDarkTheme = isDarkTheme,
        isSearchMode = isSearchMode,
        searchText = searchText,
        onSearchTextChange = onSearchTextChange,
        onSearchClick = onSearchClick,
        onToggleLayout = onToggleLayout,
        onToggleTheme = onToggleTheme
    )
}