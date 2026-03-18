package com.example.libri.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.libri.R

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
            painter = if (isBookmarked) {
                painterResource(R.drawable.baseline_favorite_24)
            } else {
                painterResource(R.drawable.outline_favorite_24)
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

@Preview
@Composable
private fun BookmarkButtonPreview() {
    BookmarkButton(
        isBookmarked = true,
        onBookmarkClick = {}
    )
}