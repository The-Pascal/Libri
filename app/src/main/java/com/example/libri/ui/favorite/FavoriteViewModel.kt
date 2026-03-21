package com.example.libri.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.domain.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: BookRepository): ViewModel() {

    val favoriteBooksUiState = repository.getFavoriteBooks()
        .map {
            Log.d("TestTag", "getFavoriteBooks viewmodel")
            UiState.Success(it)
        }
        .catch {
            UiState.Error(it.message ?: "Something went wrong")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun removeBookFromFavorites(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book.copy(isBookmarked = false))
        }
    }
}