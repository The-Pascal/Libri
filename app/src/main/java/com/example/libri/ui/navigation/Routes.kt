package com.example.libri.ui.navigation

import com.example.libri.domain.models.Book
import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Search: Routes()

    @Serializable
    data object Home: Routes()

    @Serializable
    data object Favorite: Routes()

    @Serializable
    data object Profile: Routes()

    @Serializable
    data class BookDetail(
        val bookId: String,
        val apiType: String,
        val bookName: String,
        val bookImageUrl: String,
        val authors: String,
        val isbn13: String,
        val isbn10: String
    ): Routes()
}