package com.example.libri.data.mapper

import com.example.libri.data.remote.dto.BookDto
import com.example.libri.domain.models.Book

fun BookDto.toDomain(): Book {
    return Book(
        id = this.id.removePrefix("/works/"),
        title = this.title,
        authors = this.authors ?: emptyList(),
        coverUrl = this.coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" },
        publishYear = this.year?.toString() ?: "N/A"
    )
}