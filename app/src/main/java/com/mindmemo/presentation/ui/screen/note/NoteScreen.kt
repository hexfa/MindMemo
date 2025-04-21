package com.mindmemo.presentation.ui.screen.note

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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindmemo.R
import com.mindmemo.presentation.ui.widget.CustomDialog
import com.mindmemo.presentation.ui.widget.CustomDropdownMenu
import com.mindmemo.presentation.ui.widget.DropdownMenuItemData
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
            title = stringResource(id = R.string.update_note),
            description = stringResource(id = R.string.are_you_sure_you_want_to_exit_without_saving),
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
                    hint = "${stringResource(id = R.string.title)} ...",
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
                                contentDescription = stringResource(id = R.string.expand_priority),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        CustomDropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false },
                            menuItems = viewModel.prioritiesList.map { priority ->
                                DropdownMenuItemData(
                                    text = priority,
                                    onClick = {
                                        viewModel.onPrioritySelected(priority)
                                        priorityExpanded = false
                                    }
                                )
                            }
                        )
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
                                contentDescription = stringResource(id = R.string.expand_category),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        CustomDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false },
                            menuItems = viewModel.categoriesList.map { category ->
                                DropdownMenuItemData(
                                    text = category,
                                    onClick = {
                                        viewModel.onCategorySelected(category)
                                        categoryExpanded = false
                                    }
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FlatTextField(
                    value = detailNote.description,
                    onValueChange = viewModel::onDescriptionChanged,
                    hint = "${stringResource(id = R.string.description)} ..."
                )
            }
        }
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