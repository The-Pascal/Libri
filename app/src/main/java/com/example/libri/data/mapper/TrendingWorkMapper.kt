package com.example.libri.data.mapper

import com.example.libri.data.remote.dto.TrendingWork
import com.example.libri.domain.models.Book
import com.example.libri.utils.removeIdPrefix

fun TrendingWork.toDomainModel(): Book {
    return Book(
        id = key?.removeIdPrefix() ?: "",
        title = title ?: "Unknown Title",
        authors = authorNames ?: listOf("Unknown Author"),
        // Hero Section uses 'L' (Large), trending rows usually use 'M' (Medium)
        coverUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" } ?: "",
        publishYear = firstPublishYear?.toString() ?: "N/A",
        isBookmarked = false
    )
}