package com.example.libri.ui.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Search: Routes()

    @Serializable
    data object Home: Routes()

    @Serializable
    data object Favorite: Routes()
}