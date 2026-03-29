package com.example.libri.data.remote.dto.openlibrary

import com.google.gson.annotations.SerializedName

/** Minimal edition payload from Open Library `isbn/{isbn}.json`. */
data class OpenLibraryEditionDto(
    @SerializedName("authors") val authors: List<OpenLibraryAuthorRefDto>? = null,
)

data class OpenLibraryAuthorRefDto(
    @SerializedName("key") val key: String? = null,
)
