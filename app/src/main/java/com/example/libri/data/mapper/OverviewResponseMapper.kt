package com.example.libri.data.mapper

import com.example.libri.data.remote.dto.nyt.NytBook
import com.example.libri.data.remote.dto.nyt.OverviewResponse
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookImageLinks
import com.example.libri.utils.ApiType
import com.example.libri.utils.BestSellerList

fun OverviewResponse.toDomain(): Map<BestSellerList, List<Book>> {
    return this.results.lists.associate { nytList ->
        val bestSeller = BestSellerList.fromEncodedName(nytList.listNameEncoded)
        val books = nytList.books.map { it.toDomain() }
        bestSeller to books
    }
}

fun NytBook.toDomain(): Book {
    return Book(
        apiType = ApiType.NYT,
        id = this.title,
        title = this.title,
        authors = listOf(this.author),
        coverUrl = this.bookImage,
        publishYear = this.createdDate.take(3),
        averageRating = null,
        ratingsCount = null,
        isBookmarked = false,
        isbn13 = this.isbn13,
        isbn10 = this.isbn10,
        imageLinks = BookImageLinks(
            small = this.bookImage,
            medium = this.bookImage,
            large = this.bookImage
        )
    )
}
