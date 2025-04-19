package com.mindmemo.presentation.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindmemo.presentation.ui.widget.CustomCircleIcon
import com.mindmemo.presentation.ui.widget.CustomDialog
import com.mindmemo.presentation.viewmodel.NoteViewModel

@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel(),
    noteId: Int?
) {
    val detailNote by viewModel.detailNote.collectAsState()
    var priorityExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    val hasChanges by viewModel.hasUnsavedChanges.collectAsState()
    var showUnsavedChangesDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        noteId?.let { viewModel.getDetail(it) }
    }

    BackHandler {
        if (hasChanges) {
            showUnsavedChangesDialog = true
        } else {
            navController.popBackStack()
        }
    }

    if (showUnsavedChangesDialog) {
        CustomDialog(
            title = "Update Note",
            description = "Are you sure you want to exit without saving?",
            confirmButton = {
                if (noteId != null) {
                    viewModel.updateNote()
                } else {
                    viewModel.saveNote()
                }
                showUnsavedChangesDialog = false
                navController.popBackStack()
            },
            dismissButton = {
                showUnsavedChangesDialog = false
            },
            onDismissRequest = {
                showUnsavedChangesDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            NoteTopAppBar(
                noteId = noteId,
                navController = navController,
                viewModel = viewModel
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                FlatTextField(
                    value = detailNote.title,
                    onValueChange = viewModel::onTitleChanged,
                    hint = "Title ...",
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = detailNote.dateCreated,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    //priority
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { priorityExpanded = true }
                                .background(
                                    MaterialTheme.colorScheme.onTertiaryContainer,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(
                                    horizontal = 8
                                        .dp, vertical = 4.dp
                                )
                        ) {
                            Text(
                                text = detailNote.priority,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expand Priority",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
                        ) {
                            viewModel.prioritiesList.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.onPrioritySelected(item)
                                        priorityExpanded = false
                                    },
                                    text = { Text(item) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Category
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { categoryExpanded = true }
                                .background(
                                    MaterialTheme.colorScheme.onTertiaryContainer,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = detailNote.category,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expand Category",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
                        ) {
                            viewModel.categoriesList.forEach { item ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.onCategorySelected(item)
                                        categoryExpanded = false
                                    },
                                    text = { Text(item) }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FlatTextField(
                    value = detailNote.description,
                    onValueChange = viewModel::onDescriptionChanged,
                    hint = "Description ..."
                )
            }
        }
    )
}

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
    val hasChanges by viewModel.hasUnsavedChanges.collectAsState()
    var showUnsavedChangesDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        CustomDialog(
            title = "Delete Note",
            description = "Are you sure you want to delete this note?",
            confirmButton = {
                showDeleteDialog = false
                if (noteId != null) {
                    viewModel.deleteNote(noteId)
                    navController.popBackStack()
                }
            },
            onDismissRequest = { showDeleteDialog = false },
            dismissButton = { showDeleteDialog = false })
    }

    if (showUnsavedChangesDialog) {
        CustomDialog(
            title = "Update Note",
            description = "Are you sure you want to exit without saving?",
            confirmButton = {
                if (noteId != null) {
                    viewModel.updateNote()
                } else {
                    viewModel.saveNote()
                }
                showUnsavedChangesDialog = false
                navController.popBackStack()
            },
            dismissButton = {
                showUnsavedChangesDialog = false
            },
            onDismissRequest = {
                showUnsavedChangesDialog = false
            }
        )
    }

    Box(modifier = Modifier.padding(12.dp)) {
        TopAppBar(
            title = {},
            navigationIcon = {
                CustomCircleIcon(icon = Icons.Filled.ArrowLeft, description = "BACK", onClick = {
                    if (hasChanges) {
                        showUnsavedChangesDialog = true
                    } else navController.popBackStack()
                })
            },
            actions = {
                if (hasChanges) {
                    CustomCircleIcon(icon = Icons.Default.Check, description = "OK", onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()

                        if (noteId != null) {
                            viewModel.updateNote()
                        } else {
                            viewModel.saveNote()
                        }
                    })

                }
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    CustomCircleIcon(
                        icon = Icons.Default.MoreVert,
                        description = "More",
                        onClick = { showMenu = true })
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun FlatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    maxLines: Int = Int.MAX_VALUE
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = LocalTextStyle.current.copy(color = Color.Gray)
                    )
                }
                innerTextField()
            }
        )
    }
}