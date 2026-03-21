package com.example.libri.ui.favorite

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.ui.common.FavoriteBookItem
import com.example.libri.ui.common.LibriTopAppBar
import com.example.libri.ui.common.LoadingView
import com.example.libri.ui.common.dialogs.RemoveFavoriteDialog
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.LightCharcoal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteBooks by viewModel.favoriteBooksUiState.collectAsStateWithLifecycle()

    MainContent(
        modifier = modifier,
        favoriteBooks = favoriteBooks,
        removeBookFromFavorites = { viewModel.removeBookFromFavorites(it) }
    )
}

@Composable
private fun FavoriteHeader() {
    Column {
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.displayLarge,
        )

        Text(
            text = "Your curated collection of literary gems.",
            style = MaterialTheme.typography.headlineSmall,
            color = LightCharcoal,
        )
    }
}

@Composable
private fun MainContent(
    favoriteBooks: UiState,
    removeBookFromFavorites: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { LibriTopAppBar() },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        var showRemoveFavoriteDialog by remember { mutableStateOf<Book?>(null) }
        showRemoveFavoriteDialog?.let { book ->
            RemoveFavoriteDialog(
                book = book,
                onDismissRequest = { showRemoveFavoriteDialog = null },
                onRemoveFavorite = {
                    removeBookFromFavorites(it)
                    showRemoveFavoriteDialog = null
                }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                FavoriteHeader()
            }

            when(favoriteBooks) {
                is UiState.Error -> TODO()
                is UiState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingView()
                    }
                }
                is UiState.Success -> {
                    items(
                        items = favoriteBooks.books,
                        key = { it.id }
                    ) {
                        FavoriteBookItem(
                            book = it,
                            onRemoveFavorite = { book ->
                                showRemoveFavoriteDialog = book
                            }
                        )
                    }
                }
            }
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
    LibriTheme {
        MainContent(
            favoriteBooks = UiState.Success(bookList),
            removeBookFromFavorites = {}
        )
    }
}
