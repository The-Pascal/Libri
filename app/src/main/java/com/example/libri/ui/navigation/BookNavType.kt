package com.example.libri.ui.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.example.libri.domain.models.Book
import kotlinx.serialization.json.Json

val BookNavType = object : NavType<Book>(isNullableAllowed = false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: Book
    ) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): Book? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Book {
        return Json.decodeFromString(value)
    }

}