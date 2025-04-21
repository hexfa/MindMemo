package com.mindmemo.presentation.ui.screen.note

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mindmemo.R
import com.mindmemo.presentation.ui.widget.CustomDialog

@Composable
fun DeleteNoteDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        CustomDialog(
            title = stringResource(id = R.string.delete_note),
            description = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_note),
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
            title = stringResource(id = R.string.update_note),
            description = stringResource(id = R.string.are_you_sure_you_want_to_exit_without_saving),
            confirmButton = onConfirm,
            dismissButton = onDismiss,
            onDismissRequest = onDismiss
        )
    }
}