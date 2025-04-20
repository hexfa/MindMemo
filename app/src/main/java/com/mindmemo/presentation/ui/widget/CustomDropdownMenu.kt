package com.mindmemo.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    menuItems: List<DropdownMenuItemData>,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(MaterialTheme.colorScheme.onBackground)
    ) {
        menuItems.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = item.onClick,
                leadingIcon = item.icon?.let {
                    {
                        Icon(
                            imageVector = it,
                            contentDescription = item.contentDescription
                        )
                    }
                }
            )
        }
    }
}

data class DropdownMenuItemData(
    val text: String,
    val icon: ImageVector? = null,
    val contentDescription: String? = null,
    val onClick: () -> Unit
)