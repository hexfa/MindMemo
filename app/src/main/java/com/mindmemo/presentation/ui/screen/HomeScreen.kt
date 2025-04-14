package com.mindmemo.presentation.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.presentation.viewmodel.HomeViewModel
import com.mindmemo.presentation.viewmodel.ThemeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    navController: NavController
) {
    val notesState by viewModel.getAllNotes.collectAsState(initial = null)
    val searchState by viewModel.searchNotes.collectAsState(initial = null)
    val isGrid by viewModel.isGridView.collectAsState(initial = true)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    var isSearchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    BackHandler(enabled = isSearchMode) {
        keyboardController?.hide()
        searchText = ""
        isSearchMode = false
    }

    Scaffold(
        topBar = {
            CustomToolbar(
                isGrid = isGrid,
                isDarkTheme = isDarkTheme,
                isSearchMode = isSearchMode,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    viewModel.searchNote(it)
                },
                onSearchClick = {
                    if (isSearchMode) {
                        searchText = ""
                    }
                    isSearchMode = !isSearchMode
                },
                onToggleLayout = { viewModel.toggleGridView(!isGrid) },
                onToggleTheme = { themeViewModel.toggleTheme(!isDarkTheme) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("note") },
                shape = RoundedCornerShape(50),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Note",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        val notes = if (searchText.isNotBlank()) {
            searchState?.data ?: emptyList()
        } else {
            notesState?.data ?: emptyList()
        }

        NoteListContent(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            notes = notes,
            navController = navController,
            isGrid = isGrid
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomToolbar(
    isGrid: Boolean,
    isDarkTheme: Boolean,
    isSearchMode: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onToggleLayout: () -> Unit,
    onToggleTheme: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            AnimatedContent(
                targetState = isSearchMode,
                transitionSpec = {
                    fadeIn() with fadeOut()
                },
                label = "Search vs Title"
            ) { targetIsSearch ->
                if (targetIsSearch) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = onSearchTextChange,
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                onSearchTextChange("")
                                onSearchClick()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear Search")
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text("Notes", color = Color.White)
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }

            IconButton(
                onClick = onToggleLayout,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = if (isGrid) Icons.Filled.ViewList else Icons.Filled.GridView,
                    contentDescription = "Toggle Layout",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Filled.Brightness7 else Icons.Filled.Brightness2,
                    contentDescription = "Toggle Theme",
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NoteListContent(
    notes: List<MemoEntity>,
    navController: NavController,
    modifier: Modifier = Modifier,
    isGrid: Boolean
) {
    AnimatedContent(
        targetState = isGrid,
        transitionSpec = {
            fadeIn() with fadeOut()
        },
        label = "Grid/List Animation"
    ) { targetIsGrid ->
        if (notes.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No notes yet!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            if (targetIsGrid) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = modifier.fillMaxSize(),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(notes.size) { index ->
                        val note = notes[index]
                        NoteItem(note = note, navController = navController)
                    }
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes.size) { index ->
                        NoteItem(note = notes[index], navController = navController)
                    }
                }
            }
        }
    }
}

fun getPriorityColor(priority: String): Color {
    return when (priority) {
        NORMAL -> Color.Yellow
        HIGH -> Color.Red
        else -> Color.Green
    }
}

@Composable
fun NoteItem(
    note: MemoEntity,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { navController.navigate("note?noteId=${note.id}") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(getPriorityColor(note.priority))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.disc,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.category,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}