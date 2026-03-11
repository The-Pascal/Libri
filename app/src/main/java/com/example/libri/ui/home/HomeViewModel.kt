package com.example.libri.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.domain.models.Book
import com.example.libri.domain.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(System.currentTimeMillis())

    @OptIn(ExperimentalCoroutinesApi::class)
    val trendingBooks = refreshTrigger
        .flatMapLatest {
            getTrendingFlow()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TrendingBooksUiState.Loading
        )

    private fun getTrendingFlow() = flow {
        emit(TrendingBooksUiState.Loading)
        repository.getTrendingBooks()
            .onSuccess { emit(TrendingBooksUiState.Success(it)) }
            .onFailure { emit(TrendingBooksUiState.Error("Something went wrong")) }
    }

    fun onRetryClick() {
        refreshTrigger.value = System.currentTimeMillis()
    }
}

sealed class TrendingBooksUiState {
    data object Loading : TrendingBooksUiState()
    data class Error(val message: String) : TrendingBooksUiState()
    data class Success(val books: List<Book>) : TrendingBooksUiState()
}