package com.example.libri.data.mapper

import android.util.Log
import com.example.libri.data.mapper.extensions.toHttps
import com.example.libri.data.remote.dto.google.BookVolume
import com.example.libri.data.remote.dto.google.GoogleBooksResponse
import com.example.libri.data.remote.dto.google.VolumeInfo
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookImageLinks
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