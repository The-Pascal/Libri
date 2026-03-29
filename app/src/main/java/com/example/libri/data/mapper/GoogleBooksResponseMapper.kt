package com.example.libri.data.mapper

import android.util.Log
import com.example.libri.data.mapper.extensions.toHttps
import com.example.libri.data.remote.dto.google.BookVolume
import com.example.libri.data.remote.dto.google.GoogleBooksResponse
import com.example.libri.data.remote.dto.google.VolumeInfo
import com.example.libri.domain.models.Authors
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookDetails
import com.example.libri.domain.models.BookImageLinks
import com.example.libri.domain.models.BookStats
import com.example.libri.utils.ApiType

fun GoogleBooksResponse.toDomain(): List<Book> {
    return this.items?.map {
        it.toDomain()
    } ?: emptyList()
}

fun BookVolume.toDomain(): Book {
    val bookImageLinks = getBookImageLinks(this.volumeInfo)
    Log.d("ImageLinks", "Book links - $bookImageLinks")
    return Book(
        apiType = ApiType.GOOGLE,
        id = this.id,
        title = this.volumeInfo?.title ?: "Unknown title",
        authors = this.volumeInfo?.authors ?: emptyList(),
        coverUrl = this.volumeInfo?.imageLinks?.thumbnail.toHttps,
        publishYear = this.volumeInfo?.publishedDate ?: "NA",
        averageRating = this.volumeInfo?.averageRating,
        ratingsCount = this.volumeInfo?.ratingsCount,
        imageLinks = bookImageLinks,

        // User Edited fields
        isBookmarked = false
    )
}

fun BookVolume.toBookDetails(): BookDetails {
    val info = volumeInfo
    val isbn = info.pickIsbn()
    return BookDetails(
        authors = info.authorsOrEmpty().map { name ->
            Authors(authorName = name, authorPhotoUrl = "", authorBio = "")
        },
        tags = info?.categories
            ?.flatMap { it.split("/") }
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.distinct(),
        stats = BookStats(
            pages = info?.pageCount?.toString().orEmpty().ifBlank { "—" },
            languages = info?.language?.uppercase().orEmpty().ifBlank { "—" },
            isbn = isbn.ifBlank { "—" },
            publishedDate = info?.publishedDate.orEmpty().ifBlank { "—" },
        ),
        description = info?.description.orEmpty().ifBlank { "No description available." },
    )
}

private fun VolumeInfo?.authorsOrEmpty(): List<String> =
    this?.authors?.map { it.trim() }?.filter { it.isNotEmpty() }.orEmpty()

private fun VolumeInfo?.pickIsbn(): String {
    val ids = this?.industryIdentifiers.orEmpty()
    val isbn13 = ids.find { it.type == "ISBN_13" }?.identifier?.filter { it.isDigit() }
    val isbn10 = ids.find { it.type == "ISBN_10" }?.identifier?.filter { it.isDigit() }
    return when {
        !isbn13.isNullOrBlank() -> isbn13
        !isbn10.isNullOrBlank() -> isbn10
        else -> ""
    }
}

private fun getBookImageLinks(volumeInfo: VolumeInfo?): BookImageLinks {
    val isbn = volumeInfo?.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier
        ?: volumeInfo?.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier

    return if (isbn != null) {
        val coverUrl = "https://covers.openlibrary.org/b/isbn/$isbn-{size}.jpg"
        BookImageLinks(
            small = coverUrl.replace("{size}", "S"),
            medium = coverUrl.replace("{size}", "M"),
            large = coverUrl.replace("{size}", "L")
        )
    } else {
        BookImageLinks(
            small = volumeInfo?.imageLinks?.smallThumbnail?.replace("http://", "https://"),
            medium = volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://"),
            large = volumeInfo?.imageLinks?.large?.replace("http://", "https://")
        )
    }
}