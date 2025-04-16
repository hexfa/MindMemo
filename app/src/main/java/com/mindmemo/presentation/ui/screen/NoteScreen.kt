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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
                    Text(text = detailNote.dateCreated, modifier = Modifier.weight(1f))
                    //priority
                    Box {
                        Text(
                            text = detailNote.priority,
                            modifier = Modifier
                                .clickable { priorityExpanded = true }
                                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                        DropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false }
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
                    Spacer(modifier = Modifier.height(16.dp))
                    // Category
                    Box {
                        Text(
                            text = detailNote.category,
                            modifier = Modifier
                                .clickable { categoryExpanded = true }
                                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )

                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
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

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                if (hasChanges) {
                    showUnsavedChangesDialog = true
                } else navController.popBackStack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "BACK")
            }
        },
        actions = {
            if (hasChanges) {
                IconButton(onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    if (noteId != null) {
                        viewModel.updateNote()
                    } else {
                        viewModel.saveNote()
                    }
                }) {
                    Icon(Icons.Default.Check, contentDescription = "OK")
                }
            }
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
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
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
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