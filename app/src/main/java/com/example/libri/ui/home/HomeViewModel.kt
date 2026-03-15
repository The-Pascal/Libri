package com.example.libri.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.domain.repository.BookRepository
import com.example.libri.utils.BookGenre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _refreshTrigger = MutableStateFlow(System.currentTimeMillis())

    private val _selectedGenre = MutableStateFlow(BookGenre.FANTASY)
    val selectedGenre = _selectedGenre.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val trendingBooks = _refreshTrigger
        .flatMapLatest {
            getTrendingFlow()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val booksByGenre: StateFlow<UiState> =
        combine(_refreshTrigger, _selectedGenre) { _, genre ->
            genre
        }.flatMapLatest { genre ->
            getBooksByGenreFlow(genre)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun onRetryClick() {
        _refreshTrigger.value = System.currentTimeMillis()
    }

    fun onGenreSelected(genre: BookGenre) {
        _selectedGenre.value = genre
    }

    private fun getTrendingFlow() = flow {
        emit(UiState.Loading)
        repository.getTrendingBooks()
            .onSuccess { emit(UiState.Success(it)) }
            .onFailure { emit(UiState.Error("Something went wrong")) }
    }

    private fun getBooksByGenreFlow(genre: BookGenre) = flow {
        emit(UiState.Loading)
        repository.getBooksByGenre(genre.apiValue)
            .onSuccess { emit(UiState.Success(it)) }
            .onFailure { emit(UiState.Error("Something went wrong")) }
    }
}
