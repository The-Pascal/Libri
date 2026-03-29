package com.example.libri.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libri.domain.models.Book
import com.example.libri.utils.ApiType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class BookDetailViewModel(
    private val args: Args
): ViewModel() {

    private val _bookArgs = MutableStateFlow(args)
    val bookArgs = _bookArgs.asStateFlow()


    data class Args(
        val bookId: String,
        val apiType: ApiType,
        val bookName: String,
        val authors: String,
        val bookImageUrl: String
    )

}