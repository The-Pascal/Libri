package com.example.libri.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.domain.models.Book
import com.example.libri.domain.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            flow {
                if (query.isBlank()) {
                    emit(SearchUiState.Idle)
                    return@flow
                }

                emit(SearchUiState.Loading)
                repository.searchBooks(query).fold(
                    onSuccess = { emit(SearchUiState.Success(it)) },
                    onFailure = { emit(SearchUiState.Error(it.message ?: "Error")) }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState.Idle
        )

    fun fetchSearchResults(query: String) {
        searchQuery.value = query
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val books: List<Book>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}