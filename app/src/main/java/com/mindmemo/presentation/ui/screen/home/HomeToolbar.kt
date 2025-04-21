package com.mindmemo.presentation.ui.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindmemo.R
import com.mindmemo.presentation.ui.widget.CustomCircleIcon
import com.mindmemo.presentation.ui.widget.CustomDropdownMenu
import com.mindmemo.presentation.ui.widget.DropdownMenuItemData

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeToolbar(
    isGrid: Boolean,
    isDarkTheme: Boolean,
    isSearchMode: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onToggleLayout: () -> Unit,
    onToggleTheme: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                transitionSpec = { fadeIn() with fadeOut() },
                label = stringResource(id = R.string.search_vs_title)
            ) { targetIsSearch ->
                if (targetIsSearch) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.onBackground,
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = onSearchTextChange,
                            singleLine = true,
                            textStyle = TextStyle.Default.copy(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .weight(1f)
                        )
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                onSearchTextChange("")
                                onSearchClick()
                            }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(id = R.string.clear_search),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.onBackground,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.notes),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCircleIcon(
                icon = Icons.Default.Search,
                description = stringResource(id = R.string.search),
                onClick = onSearchClick
            )
            Box {
                CustomCircleIcon(
                    icon = Icons.Default.MoreVert,
                    description = stringResource(id = R.string.more),
                    onClick = { expanded = true })
                CustomDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    menuItems = listOf(
                        DropdownMenuItemData(
                            text = stringResource(id = R.string.toggle_layout),
                            icon = if (isGrid) Icons.Filled.ViewList else Icons.Filled.GridView,
                            contentDescription = stringResource(id = R.string.grid_list),
                            onClick = {
                                expanded = false
                                onToggleLayout()
                            }
                        ),
                        DropdownMenuItemData(
                            text = stringResource(id = R.string.toggle_theme),
                            icon = if (isDarkTheme) Icons.Filled.Brightness7 else Icons.Filled.Brightness2,
                            contentDescription = stringResource(id = R.string.theme),
                            onClick = {
                                expanded = false
                                onToggleTheme()
                            }
                        )
                    )
                )
            }
        }
    }
}