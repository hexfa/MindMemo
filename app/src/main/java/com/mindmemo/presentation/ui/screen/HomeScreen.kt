package com.mindmemo.presentation.ui.screen

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {

    val notesState by viewModel.getAllNotes.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    Scaffold(
        topBar = {
            CustomToolbar()
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
        NoteListContent(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            notes = notesState?.data ?: emptyList(),
            navController = navController
        )
    }
}

@Composable
fun CustomToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Notes",
                color = Color.White,
            )
        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                )
            }

            IconButton(
                onClick = { },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Grid/List",
                    tint = Color.White,
                )
            }
        }
    }
}


@Composable
fun NoteListContent(
    notes: List<MemoEntity>,
    modifier: Modifier = Modifier,
    navController: NavController
) {
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
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(notes.size) { index ->
                val note = notes[index]
                NoteItem(
                    note = note, navController = navController
                )
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
            }
        }
    }
}