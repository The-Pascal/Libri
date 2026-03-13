package com.example.libri.ui.home

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.libri.R
import com.example.libri.domain.models.Book
import com.example.libri.ui.common.AuthorAvatar
import com.example.libri.ui.common.LoadingView
import com.example.libri.ui.common.SectionHeader
import com.example.libri.ui.common.ShortBookItem
import com.example.libri.utils.BookAuthor
import com.example.libri.utils.BookGenre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSearchScreen: () -> Unit,
    viewModel: HomeViewModel,
) {
    val trendingState by viewModel.trendingBooks.collectAsStateWithLifecycle()
    val booksByGenre by viewModel.booksByGenre.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Libri") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            item {
                TrendingNowSection(trendingState)
            }

            item {
                TrendingByGenreSection(
                    state = booksByGenre,
                    selectedGenre = selectedGenre,
                    onGenreSelected = { viewModel.onGenreSelected(it) }
                )
            }

            item {
                PopularAuthorsSection()
            }
        }
    }
}

@Composable
private fun TrendingNowSection(
    uiState: UiState,
) {
    Column {
        SectionHeader(
            text = "Trending Now",
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ShortItemsList(uiState)
    }
}

@Composable
private fun TrendingByGenreSection(
    state: UiState,
    selectedGenre: BookGenre,
    onGenreSelected: (BookGenre) -> Unit
) {
    Column {
        SectionHeader(
            text = "Trending by Genre",
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(
                items = BookGenre.entries,
                key = { it.apiValue }
            ) { genre ->
                FilterChip(
                    label = {
                        Text(
                            text = genre.displayName,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = genre == selectedGenre,
                    shape = RoundedCornerShape(percent = 50),
                    onClick = { onGenreSelected(genre) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        ShortItemsList(state)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PopularAuthorsSection(
    modifier: Modifier = Modifier,
    isPreview: Boolean = false
) {
    Column(modifier) {
        SectionHeader(
            text = "Popular Authors",
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = BookAuthor.all) {
                AuthorAvatar(
                    model = if (isPreview) R.drawable.demo_book_cover else it.imageUrl,
                    name = it.displayName
                )
            }
        }
    }
}

@Composable
private fun ShortItemsList(
    state: UiState,
) {
    when (state) {
        is UiState.Error -> Text("Error")

        is UiState.Loading -> LoadingView()

        is UiState.Success -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(items = state.books) {
                    ShortBookItem(book = it)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun PopularAuthorsPreview() {
    PopularAuthorsSection(isPreview = true)
}

@Preview(showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun TestPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = true
    )

    TrendingByGenreSection(
        state = UiState.Success(listOf(book, book, book)),
        selectedGenre = BookGenre.FANTASY,
        onGenreSelected = {}
    )
}

@Preview(showBackground = true, backgroundColor = Color.WHITE.toLong())
@Composable
private fun TrendingNowSectionPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = true
    )

    TrendingNowSection(
        uiState = UiState.Success(listOf(book, book, book)),
    )
}
