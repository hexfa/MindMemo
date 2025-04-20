package com.mindmemo.presentation.ui.screen.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.HOME
import com.mindmemo.presentation.ui.theme.AppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun emptyNotes_showsNoNotesMessage() {
        composeTestRule.setContent {
            AppTheme {
                NoteListContent(
                    notes = emptyList(),
                    navController = navController,
                    isGrid = false
                )
            }
        }

        composeTestRule.onNodeWithText("No notes yet!").assertIsDisplayed()
    }

    @Test
    fun notesInListMode_areDisplayed() {
        val notes = listOf(
            MemoEntity(1, "Test Note 1", "Description 1", "2023-01-01", HOME, HIGH),
            MemoEntity(2, "Test Note 2", "Description 2", "2023-01-02", HOME, HIGH)
        )

        composeTestRule.setContent {
            AppTheme {
                NoteListContent(
                    notes = notes,
                    navController = navController,
                    isGrid = false
                )
            }
        }

        composeTestRule.onNodeWithText("Test Note 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note 2").assertIsDisplayed()
    }

    @Test
    fun notesInGridMode_areDisplayed() {
        val notes = listOf(
            MemoEntity(1, "Grid Note", "Grid Desc", "2023-01-01", HOME, HIGH)
        )

        composeTestRule.setContent {
            AppTheme {
                NoteListContent(
                    notes = notes,
                    navController = navController,
                    isGrid = true
                )
            }
        }

        composeTestRule.onNodeWithText("Grid Note").assertIsDisplayed()
    }
}