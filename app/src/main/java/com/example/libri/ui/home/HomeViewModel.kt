package com.example.libri.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.domain.repository.BookRepository
import com.example.libri.utils.CategoryGroup
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

    private val _selectedCategory = MutableStateFlow(CategoryGroup.FICTION)
    val selectedCategory = _selectedCategory.asStateFlow()

    private var _trendingBooks: Map<CategoryGroup, List<Book>>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val freeTreasureBooks = _refreshTrigger
        .flatMapLatest {
            getFreeTreasuresFlow()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val booksByGenre: StateFlow<UiState> =
        combine(_refreshTrigger, _selectedCategory) { _, genre ->
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

    fun onGenreSelected(group: CategoryGroup) {
        _selectedCategory.value = group
    }

    private fun getFreeTreasuresFlow() = flow {
        emit(UiState.Loading)
        repository.getFreeTreasures()
            .onSuccess { emit(UiState.Success(it)) }
            .onFailure { emit(UiState.Error("Something went wrong")) }
    }

    private fun getBooksByGenreFlow(group: CategoryGroup) = flow {
        emit(UiState.Loading)
        if (_trendingBooks == null) {
            repository.getTrendingBooks()
                .onFailure { emit(UiState.Error("Something went wrong")) }
                .onSuccess { successList ->
                    _trendingBooks = successList.entries
                        .groupBy({ it.key.group }, {it.value})
                        .mapValues { it.value.flatten() }
                }
        }

        _trendingBooks?.get(group)?.let { books ->
            emit(UiState.Success(
                books.sortedBy { it.rank }.distinctBy { it.id }
            ))
        } ?: run {
            emit(UiState.Error("Something went wrong"))
        }

    }
}
