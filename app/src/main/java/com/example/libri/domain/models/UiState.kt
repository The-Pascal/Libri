package com.example.libri.domain.models

sealed class UiState {
    data object Loading: UiState()
    data class Error(val message: String): UiState()
    data class Success(val books: List<Book>): UiState()
}