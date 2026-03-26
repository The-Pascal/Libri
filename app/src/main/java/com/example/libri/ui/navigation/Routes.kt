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
        val bookId: String
    ): Routes()
}