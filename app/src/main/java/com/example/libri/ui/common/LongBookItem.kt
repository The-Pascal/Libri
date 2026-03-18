package com.example.libri.ui.common

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.libri.domain.models.Book

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LongBookItem(
    book: Book,
    onBookmarkClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        BookImage(
            book.coverUrl,
            modifier = Modifier
                .size(width = 80.dp, height = 100.dp)
                .padding(end = 12.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = book.publishYear,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = book.authors.joinToString(),
                style = MaterialTheme.typography.labelSmall
            )
        }

        BookmarkButton(
            isBookmarked = book.isBookmarked,
            onBookmarkClick = {
                Log.d(TAG, "onBookmarkClicked - ${book.title}")
                onBookmarkClick(book)
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun LongBookItemPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = false
    )
    LongBookItem(book, {})
}

private const val TAG = "BookItem"
