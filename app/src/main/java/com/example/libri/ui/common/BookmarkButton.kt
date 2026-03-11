package com.example.libri.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onBookmarkClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isBookmarked) {
                Icons.Filled.Favorite
            } else {
                Icons.Outlined.Favorite
            },
            contentDescription = if (isBookmarked) "Remove Bookmark" else "Add Bookmark",
            tint = if (isBookmarked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}
