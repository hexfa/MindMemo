package com.mindmemo.presentation.ui.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.mindmemo.presentation.ui.theme.Green

@Composable
fun CustomDialog(
    title: String,
    description: String,
    confirmButton: () -> Unit,
    dismissButton: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = { Text(title) },
        text = { Text(description) },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmButton()
                }) {
                Text("OK", color = Green)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismissButton()
                }
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}