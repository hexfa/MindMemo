package com.mindmemo.presentation.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindmemo.R
import com.mindmemo.presentation.ui.navigation.NOTE
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
            HomeToolbar(
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
                onClick = { navController.navigate(NOTE) },
                shape = RoundedCornerShape(50),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_note),
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