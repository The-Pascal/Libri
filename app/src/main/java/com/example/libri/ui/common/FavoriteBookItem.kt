package com.example.libri.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.libri.R
import com.example.libri.domain.models.Book
import com.example.libri.ui.theme.LibriTheme

@Composable
fun FavoriteBookItem(
    book: Book,
    onRemoveFavorite: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Box() {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            BookImage(
                url = book.coverUrl,
                modifier = Modifier
                    .aspectRatio(2f / 3f)
                    .clip(MaterialTheme.shapes.medium)
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = book.authors.joinToString(),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    onClick = { onRemoveFavorite(book) }
                )
        ) {
            FavoriteCircularButton(book.isBookmarked)
        }
    }
}

@Composable
fun FavoriteCircularButton(isFavorite: Boolean) {
    Surface(
        modifier = Modifier.padding(12.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
    ) {
        Icon(
            painter = if (isFavorite) {
                painterResource(R.drawable.baseline_favorite_24)
            } else {
                painterResource(R.drawable.outline_favorite_24)
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(8.dp).size(20.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun FavoriteBookItemPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = true
    )
    val bookList = listOf(
        book, book.copy(id = "abc")
    )

    LibriTheme {
        Surface() {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = bookList,
                    key = { it.id }
                ) {
                    FavoriteBookItem(book = it, {})
                }
            }
        }
    }
}