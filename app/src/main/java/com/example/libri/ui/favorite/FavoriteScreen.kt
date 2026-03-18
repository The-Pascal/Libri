package com.example.libri.ui.favorite

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.ui.common.DismissBackground
import com.example.libri.ui.common.LoadingView
import com.example.libri.ui.common.LongBookItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteBooks by viewModel.getFavoriteBooks().collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Your Favorites") })
        }
    ) { innerPadding ->
        MainContent(
            uiState = favoriteBooks,
            onRemoveItem = { viewModel.removeBookFromFavorites(it) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MainContent(
    uiState: UiState,
    onRemoveItem: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        when(uiState) {
            is UiState.Error -> {
                Text("Error")
            }
            is UiState.Loading -> {
                LoadingView()
            }
            is UiState.Success -> {
                LazyColumn {
                    items(items = uiState.books, key = { it.id }) {
                        SwipeableBookItem(
                            book = it,
                            onRemove = onRemoveItem
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeableBookItem(
    book: Book,
    onRemove: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onRemove(book)
                true
            } else {
                true
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState) },
        enableDismissFromStartToEnd = false,
        modifier = modifier.padding(bottom = 2.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
        ) {
            LongBookItem(book, onBookmarkClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun MainContentPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = true
    )
    val bookList = listOf(
        book,
        book.copy(id = "123")
    )
    MainContent(uiState = UiState.Success(bookList), {})
}