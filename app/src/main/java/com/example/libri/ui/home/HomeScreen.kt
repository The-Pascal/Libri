package com.example.libri.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.ui.common.BookItem
import com.example.libri.ui.common.LoadingView
import com.example.libri.ui.common.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSearchScreen: () -> Unit,
    viewModel: HomeViewModel,
) {
    val trendingState by viewModel.trendingBooks.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Libri")
                }
            )
        }
    ) { innerPadding: PaddingValues ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(
                horizontal = 16.dp
            )
        ) {
            item { SectionHeader("Trending Books") }
            trendingList(trendingState)
        }
    }
}

private fun LazyListScope.trendingList(state: TrendingBooksUiState) {
    when (state) {
        is TrendingBooksUiState.Error -> item {
            Text("Error")
        }
        is TrendingBooksUiState.Loading -> item {
            LoadingView()
        }
        is TrendingBooksUiState.Success -> {
            item {
                LazyRow {
                    items(items = state.books) {
                        BookItem(book = it)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestPreview() {
    LazyColumn {
        trendingList(TrendingBooksUiState.Loading)
    }
}
