package com.mindmemo.presentation.ui.screen.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.WORK
import com.mindmemo.presentation.ui.screen.home.NoteItem
import com.mindmemo.presentation.ui.theme.AppTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class NoteItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noteItem_displaysData_andNavigatesOnClick() {
        val note = MemoEntity(
            id = 123,
            title = "Test Note",
            description = "Note description",
            dateCreated = "2025-04-20",
            priority = HIGH,
            category = WORK
        )

        val mockNavController = mockk<NavController>(relaxed = true)

        composeTestRule.setContent {
            AppTheme {
                NoteItem(note = note, navController = mockNavController)
            }
        }

        composeTestRule.onNodeWithText("Test Note").assertExists().performClick()

        verify {
            mockNavController.navigate("note?noteId=123")
        }
    }
}