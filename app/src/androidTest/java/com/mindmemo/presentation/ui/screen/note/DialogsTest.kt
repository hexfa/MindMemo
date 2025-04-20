package com.mindmemo.presentation.ui.screen.note

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mindmemo.presentation.ui.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NoteDialogsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun deleteNoteDialog_showsCorrectText_andRespondsToClick() {
        var confirmCalled = false
        var dismissCalled = false

        composeTestRule.setContent {
            AppTheme {
                DeleteNoteDialog(
                    showDialog = true,
                    onConfirm = { confirmCalled = true },
                    onDismiss = { dismissCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Delete Note").assertExists()
        composeTestRule.onNodeWithText("Are you sure you want to delete this note?").assertExists()

        composeTestRule.onNodeWithText("Confirm").performClick()
        assertTrue(confirmCalled)

        composeTestRule.setContent {
            AppTheme {
                DeleteNoteDialog(
                    showDialog = true,
                    onConfirm = {},
                    onDismiss = { dismissCalled = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Dismiss").performClick()
        assertTrue(dismissCalled)
    }

    @Test
    fun unsavedChangesDialog_showsCorrectText_andRespondsToClick() {
        var confirmCalled = false
        var dismissCalled = false

        composeTestRule.setContent {
            AppTheme {
                UnsavedChangesDialog(
                    showDialog = true,
                    onConfirm = { confirmCalled = true },
                    onDismiss = { dismissCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Update Note").assertExists()
        composeTestRule.onNodeWithText("Are you sure you want to exit without saving?")
            .assertExists()

        composeTestRule.onNodeWithText("Confirm").performClick()
        assertTrue(confirmCalled)

        composeTestRule.setContent {
            AppTheme {
                UnsavedChangesDialog(
                    showDialog = true,
                    onConfirm = {},
                    onDismiss = { dismissCalled = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Dismiss").performClick()
        assertTrue(dismissCalled)
    }
}