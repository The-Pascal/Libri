package com.example.libri.ui.search

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.R
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.ui.common.LongBookItem
import com.example.libri.ui.common.ErrorView
import com.example.libri.ui.common.LoadingView
import com.example.libri.ui.common.SectionHeader
import com.example.libri.ui.common.ShortBookItem
import com.example.libri.ui.common.ShortBookItemShimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val hotNewReleases by searchViewModel.hotNewReleases.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SimpleSearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    searchViewModel.fetchSearchResults(it)
                },
            )

            Box {
                when (val searchState = searchUiState) {
                    is SearchUiState.Error -> ErrorView(searchState.message)
                    is SearchUiState.Idle -> IdleState(hotNewReleases)
                    is SearchUiState.Loading -> LoadingView()
                    is SearchUiState.Success -> BooksList(
                        booksList = searchState.books,
                        onBookmarkClicked = { searchViewModel.insertToFavoriteDB(it) }
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    expandedState: Boolean = false
) {
    var expanded by rememberSaveable { mutableStateOf(expandedState) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { onQueryChange(it) },
                onSearch = { onQueryChange(it) },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search") },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = null
                    )
                }
            )
        },
        expanded = false,
        onExpandedChange = {
            expanded = it
        },
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) { }
}

@Composable
private fun IdleState(hotNewReleases: UiState) {
    Column {
        SectionHeader(
            text = "Latest Publications",
            modifier = Modifier.padding(start = 16.dp, top = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ShortItemsList(hotNewReleases)
    }
}

@Composable
private fun ShortItemsList(
    state: UiState,
) {
    when (state) {
        is UiState.Error -> Text("Error")

        is UiState.Loading -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(5) {
                    ShortBookItemShimmer()
                }
            }
        }

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

@Composable
private fun BooksList(
    booksList: List<Book>,
    onBookmarkClicked: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 8.dp, bottom = 8.dp
        )
    ) {
        items(
            items = booksList,
            key = { it.id }
        ) { book ->
            LongBookItem(
                book = book,
                onBookmarkClick = { onBookmarkClicked(book.copy(isBookmarked = !book.isBookmarked)) }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenPreview() {
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

    SimpleSearchBar(
        query = "Harry",
        onQueryChange = { },
        expandedState = true
    )
}