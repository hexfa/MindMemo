package com.mindmemo.presentation.ui.screen.note

import androidx.compose.runtime.Composable
import com.mindmemo.presentation.ui.widget.CustomDialog

@Composable
fun DeleteNoteDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        CustomDialog(
            title = "Delete Note",
            description = "Are you sure you want to delete this note?",
            confirmButton = onConfirm,
            dismissButton = onDismiss,
            onDismissRequest = onDismiss
        )
    }
}

@Composable
fun UnsavedChangesDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        CustomDialog(
            title = "Update Note",
            description = "Are you sure you want to exit without saving?",
            confirmButton = onConfirm,
            dismissButton = onDismiss,
            onDismissRequest = onDismiss
        )
    }
}