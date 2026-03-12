package com.example.libri.data.mapper

import com.example.libri.data.remote.dto.SubjectWorkDto
import com.example.libri.domain.models.Book
import com.example.libri.utils.removeIdPrefix

fun SubjectWorkDto.toDomain(): Book {
    return Book(
        id = this.key?.removeIdPrefix() ?: "NA",
        title = this.title ?: "Unknown",
        authors = this.authors?.map { it.name ?: "Unknown" } ?: listOf("Unknown Author"),
        coverUrl = this.coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" },
        publishYear = this.firstPublishYear?.toString() ?: "N/A",
        isBookmarked = false,
    )
}