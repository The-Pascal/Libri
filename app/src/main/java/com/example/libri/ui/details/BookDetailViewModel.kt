package com.example.libri.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookDetails
import com.example.libri.domain.repository.BookRepository
import com.example.libri.utils.ApiType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn


class BookDetailViewModel(
    args: Args,
    private val repository: BookRepository
): ViewModel() {

    private val _bookArgs = MutableStateFlow(args)
    val bookArgs = _bookArgs.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookDetails = _refreshTrigger.flatMapLatest {
        loadBookDetails()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BookDetailsUIModel.Loading
    )

    private fun loadBookDetails(): Flow<BookDetailsUIModel> = flow {
        emit(BookDetailsUIModel.Loading)
        val args = when (_bookArgs.value.apiType) {
            ApiType.GOOGLE -> BookRepositoryImpl.GetBookDetailsType.Google(_bookArgs.value.bookId)
            ApiType.GUTENDEX -> BookRepositoryImpl.GetBookDetailsType.Gutendex(_bookArgs.value.bookName, _bookArgs.value.authors)
            ApiType.NYT -> BookRepositoryImpl.GetBookDetailsType.NYT(_bookArgs.value.isbn13, _bookArgs.value.isbn10)
            ApiType.UNKNOWN -> throw Exception("Unknown Book details type.")
        }
        repository.loadBookDetailsFromGoogle(args)
            .onSuccess {
                emit(BookDetailsUIModel.Success(it))
            }.onFailure {
                emit(BookDetailsUIModel.Error("Something went wrong"))
            }
    }

    data class Args(
        val bookId: String,
        val apiType: ApiType,
        val bookName: String,
        val authors: String,
        val bookImageUrl: String,
        val isbn13: String?,
        val isbn10: String?,
    )

    sealed class BookDetailsUIModel {
        object Loading: BookDetailsUIModel()
        data class Success(val bookDetails: BookDetails): BookDetailsUIModel()
        data class Error(val message: String): BookDetailsUIModel()
    }

}