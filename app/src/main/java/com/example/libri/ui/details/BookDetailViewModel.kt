package com.example.libri.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.domain.models.Authors
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
import kotlinx.coroutines.launch

class BookDetailViewModel(
    args: Args,
    private val repository: BookRepository,
) : ViewModel() {

    private val _bookArgs = MutableStateFlow(args)
    val bookArgs = _bookArgs.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(false)

    private val _authorEnrichment = MutableStateFlow<AuthorEnrichmentUi>(AuthorEnrichmentUi.Idle)
    val authorEnrichment = _authorEnrichment.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookDetails = _refreshTrigger.flatMapLatest {
        loadBookDetails()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BookDetailsUIModel.Loading,
    )

    private fun loadBookDetails(): Flow<BookDetailsUIModel> = flow {
        emit(BookDetailsUIModel.Loading)
        val argsValue = _bookArgs.value
        val repoArgs = when (argsValue.apiType) {
            ApiType.GOOGLE -> BookRepositoryImpl.GetBookDetailsType.Google(argsValue.bookId)
            ApiType.GUTENDEX -> BookRepositoryImpl.GetBookDetailsType.Gutendex(
                argsValue.bookName,
                argsValue.authors,
            )
            ApiType.NYT -> BookRepositoryImpl.GetBookDetailsType.NYT(
                isbn13 = argsValue.isbn13?.takeIf { it.isNotBlank() },
                isbn10 = argsValue.isbn10?.takeIf { it.isNotBlank() },
            )
            ApiType.UNKNOWN -> throw IllegalStateException("Unknown Book details type.")
        }
        repository.loadBookDetailsFromGoogle(repoArgs)
            .onSuccess {
                emit(BookDetailsUIModel.Success(it))
            }
            .onFailure {
                emit(BookDetailsUIModel.Error("Something went wrong"))
            }
    }

    /**
     * Lazy-loads author photos/bios from Open Library after book details are shown.
     * Safe to call multiple times; only the first in-flight request runs until completion.
     */
    fun loadAuthorBiosIfNeeded() {
        viewModelScope.launch {
            when (_authorEnrichment.value) {
                is AuthorEnrichmentUi.Loading, is AuthorEnrichmentUi.Success -> return@launch
                else -> Unit
            }
            val success = bookDetails.value as? BookDetailsUIModel.Success ?: return@launch
            _authorEnrichment.value = AuthorEnrichmentUi.Loading
            val argsValue = _bookArgs.value
            repository.enrichAuthorDetails(
                bookDetails = success.bookDetails,
                title = argsValue.bookName,
                authorsLine = argsValue.authors,
            ).fold(
                onSuccess = { authors ->
                    _authorEnrichment.value = AuthorEnrichmentUi.Success(authors)
                },
                onFailure = {
                    _authorEnrichment.value = AuthorEnrichmentUi.Failed
                },
            )
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
        data object Loading : BookDetailsUIModel()
        data class Success(val bookDetails: BookDetails) : BookDetailsUIModel()
        data class Error(val message: String) : BookDetailsUIModel()
    }

    sealed class AuthorEnrichmentUi {
        data object Idle : AuthorEnrichmentUi()
        data object Loading : AuthorEnrichmentUi()
        data class Success(val authors: List<Authors>) : AuthorEnrichmentUi()
        data object Failed : AuthorEnrichmentUi()
    }
}
