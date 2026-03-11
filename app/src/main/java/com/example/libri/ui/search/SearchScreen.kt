package com.example.libri.ui.search

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.R
import com.example.libri.domain.models.Book
import com.example.libri.ui.common.BookItem
import com.example.libri.ui.common.ErrorView
import com.example.libri.ui.common.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
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
                searchUiState = searchUiState
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    searchUiState: SearchUiState,
    expandedState: Boolean = false
) {
    var expanded by rememberSaveable { mutableStateOf(expandedState) }

    // Padding issue - https://issuetracker.google.com/issues/352872248?hl=ar&pli=1
    val animateHorizontalPadding by animateDpAsState(
        targetValue = if (expanded) 0.dp else 16.dp,
        label = "paddingAnim"
    )

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
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier.padding(horizontal = animateHorizontalPadding)
    ) {
        Box {
            when (searchUiState) {
                is SearchUiState.Error -> ErrorView(searchUiState.message)
                is SearchUiState.Idle -> Text("Idle state")
                is SearchUiState.Loading -> LoadingView()
                is SearchUiState.Success -> BooksList(searchUiState.books)
            }
        }
    }
}

@Composable
private fun BooksList(
    booksList: List<Book>,
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
        ) {
            BookItem(book = it)
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
        searchUiState = SearchUiState.Success(books = bookList),
        expandedState = true
    )
}