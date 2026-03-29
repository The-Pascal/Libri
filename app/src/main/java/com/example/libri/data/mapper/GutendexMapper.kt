package com.example.libri.data.mapper

import com.example.libri.data.remote.dto.gutendex.GutendexBookDto
import com.example.libri.data.remote.dto.gutendex.GutendexBooksPage
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookImageLinks
import com.example.libri.utils.ApiType

fun GutendexBooksPage.toDomainBooks(): List<Book> =
    results.orEmpty().map { it.toDomain() }

fun GutendexBookDto.toDomain(): Book {
    val coverUrl = formats.coverImageUrl()
    return Book(
        apiType = ApiType.GUTENDEX,
        id = id.toString(),
        title = title,
        authors = authors.orEmpty().map { it.name },
        coverUrl = coverUrl,
        publishYear = "NA",
        averageRating = null,
        ratingsCount = null,
        isBookmarked = false,
        downloadCount = downloadCount,
        imageLinks = coverUrl?.let { url ->
            BookImageLinks(
                small = url.replace("medium", "small"),
                medium = url,
                large = url
            )
        },
    )
}

/** Gutenberg medium JPEG when present. */
private fun Map<String, String>?.coverImageUrl(): String? {
    if (this == null) return null
    return this["image/jpeg"]
        ?: entries.firstOrNull { (k, v) ->
            k.startsWith("image/") && v.startsWith("http")
        }?.value
}