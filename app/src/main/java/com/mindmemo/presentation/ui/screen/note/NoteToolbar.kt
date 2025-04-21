package com.mindmemo.presentation.ui.screen.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindmemo.R
import com.mindmemo.presentation.ui.widget.CustomCircleIcon
import com.mindmemo.presentation.ui.widget.CustomDropdownMenu
import com.mindmemo.presentation.ui.widget.DropdownMenuItemData
import com.mindmemo.presentation.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopAppBar(
    noteId: Int?,
    navController: NavController,
    viewModel: NoteViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUnsavedChangesDialog by remember { mutableStateOf(false) }

    val hasChanges by viewModel.hasUnsavedChanges.collectAsState()

    DeleteNoteDialog(
        showDialog = showDeleteDialog,
        onConfirm = {
            viewModel.deleteNote(noteId ?: 0)
            navController.popBackStack()
            showDeleteDialog = false
        },
        onDismiss = { showDeleteDialog = false }
    )

    UnsavedChangesDialog(
        showDialog = showUnsavedChangesDialog,
        onConfirm = {
            if (noteId != null) {
                viewModel.updateNote()
            } else {
                viewModel.saveNote()
            }
            showUnsavedChangesDialog = false
            navController.popBackStack()
        },
        onDismiss = { showUnsavedChangesDialog = false }
    )

    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        TopAppBar(
            title = {},
            navigationIcon = {
                CustomCircleIcon(
                    icon = Icons.Filled.ArrowLeft,
                    description = stringResource(id = R.string.back),
                    onClick = {
                        if (hasChanges) {
                            showUnsavedChangesDialog = true
                        } else {
                            navController.popBackStack()
                        }
                    }
                )
            },
            actions = {
                if (hasChanges) {
                    CustomCircleIcon(
                        icon = Icons.Default.Check,
                        description = stringResource(id = R.string.ok),
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()

                            if (noteId != null) {
                                viewModel.updateNote()
                            } else {
                                viewModel.saveNote()
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    CustomCircleIcon(
                        icon = Icons.Default.MoreVert,
                        description = stringResource(id = R.string.more),
                        onClick = { showMenu = true }
                    )

                    CustomDropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        menuItems = listOf(
                            DropdownMenuItemData(
                                text = stringResource(id = R.string.delete),
                                icon = Icons.Default.DeleteOutline,
                                contentDescription = stringResource(id = R.string.delete),
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                }
                            )
                        )
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            )
        )
    }
}