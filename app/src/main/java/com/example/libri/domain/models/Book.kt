package com.example.libri.domain.models

import com.example.libri.utils.ApiType

data class Book(
    val apiType: ApiType = ApiType.UNKNOWN,
    val id: String,
    val title: String,
    val authors: List<String>,
    @Deprecated("Use imageLinks instead.")
    val coverUrl: String?,
    val publishYear: String,
    val averageRating: Double? = 0.0,
    val ratingsCount: Int? = 0,
    val isBookmarked: Boolean = false,
    val imageLinks: BookImageLinks? = null,
    val downloadCount: Int? = null,
    val tags: List<String>? = null,
    val isbn10: String? = null,
    val isbn13: String? = null
)

data class BookImageLinks(
    val small: String?,
    val medium: String?,
    val large: String?
)