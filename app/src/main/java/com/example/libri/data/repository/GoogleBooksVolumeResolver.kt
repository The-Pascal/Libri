package com.example.libri.data.repository

import com.example.libri.data.remote.GoogleBooksApi

class GoogleBooksVolumeResolver(
    private val api: GoogleBooksApi,
) {

    suspend fun resolveVolumeId(bookDetails: BookRepositoryImpl.GetBookDetailsType): Result<String> {
        return when (bookDetails) {
            is BookRepositoryImpl.GetBookDetailsType.Google -> Result.success(bookDetails.volumeId)

            is BookRepositoryImpl.GetBookDetailsType.NYT -> {
                val isbn = bookDetails.isbn13?.takeIf(::isValidIsbn13)
                    ?: bookDetails.isbn10?.takeIf(::isValidIsbn10)
                if (isbn == null) {
                    return Result.failure(IllegalStateException("No usable ISBN for NYT book"))
                }
                searchFirstVolumeId("isbn:$isbn")
            }

            is BookRepositoryImpl.GetBookDetailsType.Gutendex -> {
                val title = bookDetails.title.ifBlank { return Result.failure(IllegalStateException("Empty title")) }
                val author = bookDetails.authors
                val q = buildTitleAuthorQuery(title, author)
                searchFirstVolumeId(q)
            }

        }
    }

    private suspend fun searchFirstVolumeId(query: String): Result<String> {
        val response = api.searchBooks(query = query, maxResults = 5)
        if (!response.isSuccessful) return Result.failure(IllegalStateException("Search failed"))
        val id = response.body()?.items?.firstOrNull()?.id
        return if (id != null) Result.success(id)
        else Result.failure(IllegalStateException("No Google Books match"))
    }

    private fun buildTitleAuthorQuery(title: String, author: String): String {
        val t = title.replace("\"", "").trim()
        val a = author.replace("\"", "").trim()
        return "intitle:$t inauthor:$a"
    }

    private fun isValidIsbn13(s: String): Boolean {
        val d = s.filter { it.isDigit() }
        return d.length == 13
    }

    private fun isValidIsbn10(s: String): Boolean {
        val d = s.filter { it.isDigit() }
        return d.length == 10
    }
}