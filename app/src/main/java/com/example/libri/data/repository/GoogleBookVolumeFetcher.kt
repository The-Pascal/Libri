package com.example.libri.data.repository

import com.example.libri.data.remote.GoogleBooksApi
import com.example.libri.data.remote.dto.google.BookVolume
import com.example.libri.data.remote.dto.google.VolumeInfo

/**
 * Loads a [BookVolume] with minimal Google API calls:
 * - [BookRepositoryImpl.GetBookDetailsType.Google]: single [GoogleBooksApi.getBookDetails].
 * - NYT: ISBN search with maxResults=1; second call only if description is too short.
 * - Gutendex: title/author search (one request); optional [fetchByVolumeId] if description is short.
 */
internal class GoogleBookVolumeFetcher(
    private val api: GoogleBooksApi,
) {

    suspend fun fetchVolume(type: BookRepositoryImpl.GetBookDetailsType): Result<BookVolume> {
        return when (type) {
            is BookRepositoryImpl.GetBookDetailsType.Google -> fetchByVolumeId(type.volumeId)
            is BookRepositoryImpl.GetBookDetailsType.NYT -> fetchFromNytIsbn(type)
            is BookRepositoryImpl.GetBookDetailsType.Gutendex -> fetchFromGutendex(type)
        }
    }

    private suspend fun fetchByVolumeId(volumeId: String): Result<BookVolume> {
        if (volumeId.isBlank()) {
            return Result.failure(IllegalStateException("Empty volume id"))
        }
        val response = api.getBookDetails(volumeId)
        if (!response.isSuccessful) {
            return Result.failure(IllegalStateException("Volume request failed"))
        }
        val body = response.body() ?: return Result.failure(IllegalStateException("Empty volume body"))
        return Result.success(body)
    }

    private suspend fun fetchFromNytIsbn(type: BookRepositoryImpl.GetBookDetailsType.NYT): Result<BookVolume> {
        val isbn = type.isbn13?.normalizeIsbn()?.takeIf { it.length == 13 }
            ?: type.isbn10?.normalizeIsbn()?.takeIf { it.length == 10 }
        if (isbn == null) {
            return Result.failure(IllegalStateException("No usable ISBN for NYT book"))
        }
        val searchResponse = api.searchBooks(query = "isbn:$isbn", maxResults = NYT_ISBN_SEARCH_MAX)
        if (!searchResponse.isSuccessful) {
            return Result.failure(IllegalStateException("ISBN search failed"))
        }
        val first = searchResponse.body()?.items?.firstOrNull()
            ?: return Result.failure(IllegalStateException("No Google Books match for ISBN"))

        return if (first.volumeInfo.isDescriptionDetailedEnough()) {
            Result.success(first)
        } else {
            fetchByVolumeId(first.id)
        }
    }

    private suspend fun fetchFromGutendex(type: BookRepositoryImpl.GetBookDetailsType.Gutendex): Result<BookVolume> {
        val title = type.title.ifBlank { return Result.failure(IllegalStateException("Empty title")) }
        val q = buildTitleAuthorQuery(title, type.authors)
        val searchResponse = api.searchBooks(query = q, maxResults = GUTENDEX_SEARCH_MAX)
        if (!searchResponse.isSuccessful) {
            return Result.failure(IllegalStateException("Search failed"))
        }
        val first = searchResponse.body()?.items?.firstOrNull()
            ?: return Result.failure(IllegalStateException("No Google Books match"))

        return if (first.volumeInfo.isDescriptionDetailedEnough()) {
            Result.success(first)
        } else {
            fetchByVolumeId(first.id)
        }
    }

    private fun buildTitleAuthorQuery(title: String, author: String): String {
        val t = title.replace("\"", "").trim()
        val a = author.replace("\"", "").trim()
        return "intitle:$t inauthor:$a"
    }

    private fun VolumeInfo?.isDescriptionDetailedEnough(): Boolean {
        val description = this?.description?.trim().orEmpty()
        return description.length >= MIN_DESCRIPTION_LENGTH
    }

    private fun String.normalizeIsbn(): String = filter { it.isDigit() }

    private companion object {
        private const val MIN_DESCRIPTION_LENGTH = 48
        private const val NYT_ISBN_SEARCH_MAX = 1
        private const val GUTENDEX_SEARCH_MAX = 5
    }
}
