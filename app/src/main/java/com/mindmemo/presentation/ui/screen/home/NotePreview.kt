package com.mindmemo.presentation.ui.screen.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mindmemo.R
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.EDUCATION
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.HOME
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK

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
        label = stringResource(id = R.string.grid_list_anim)
    ) { targetIsGrid ->
        if (notes.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(id = R.string.no_notes_yet),
                    style = MaterialTheme.typography.bodyLarge
                )
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

    val iconRes = when (note.category) {
        HOME -> R.drawable.home
        WORK -> R.drawable.work
        EDUCATION -> R.drawable.education
        else -> R.drawable.health
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
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
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.dateCreated,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = note.category,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}