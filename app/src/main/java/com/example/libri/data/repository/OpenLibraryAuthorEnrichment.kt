package com.example.libri.data.repository

import android.util.Log
import com.example.libri.data.mapper.toBioText
import com.example.libri.data.remote.OpenLibraryApi
import com.example.libri.data.remote.dto.AuthorDto
import com.example.libri.domain.models.Authors
import com.example.libri.domain.models.BookDetails
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Resolves Open Library author OLIDs via a single ISBN edition lookup when possible,
 * otherwise one [OpenLibraryApi.searchBooks] call. Fetches [OpenLibraryApi.getAuthorDetails]
 * only for distinct OLIDs (parallel).
 *
 * When OLIDs come from the **ISBN edition**, display names prefer [AuthorDto.name] (catalog
 * canonical form), falling back to Google’s name if OL name is missing. Search fallback keeps
 * Google names only—order/spelling vs. OL is less reliable.
 */
internal class OpenLibraryAuthorEnrichment(
    private val api: OpenLibraryApi,
) {

    suspend fun enrich(
        bookDetails: BookDetails,
        title: String,
        authorsLine: String,
    ): Result<List<Authors>> {
        val googleNames = bookDetails.authors.map { it.authorName }
        if (googleNames.isEmpty()) {
            return Result.success(emptyList())
        }

        val isbn = bookDetails.stats.isbn.normalizeIsbnForOl()
        val olidsFromIsbn = if (!isbn.isNullOrBlank()) {
            fetchOlidsFromIsbn(isbn)
        } else {
            emptyList()
        }

        val (orderedOlids, preferOlDisplayNames) = if (olidsFromIsbn.isNotEmpty()) {
            olidsFromIsbn to true
        } else {
            fetchOlidsFromSearch(title, authorsLine) to false
        }

        if (orderedOlids.isEmpty()) {
            return Result.success(googleNames.map { Authors(it, "", "") })
        }

        val uniqueOlids = orderedOlids.distinct()
        val byOlid: Map<String, AuthorDto?> = coroutineScope {
            val result: Map<String, Deferred<AuthorDto?>> = uniqueOlids.associateWith { olid ->
                async {
                    var result: AuthorDto? = null
                    runCatching {
                        val result = api.getAuthorDetails(olid)
                        return@runCatching if (result.isSuccessful) {
                            result.body()
                        } else {
                            null
                        }
                    }.onSuccess {
                        Log.d("Author", "onSuccess - $it")
                        result = it
                    }.onFailure {
                        Log.d("Author", "onFailure - ${it.message}", it)
                    }
                    result
                }
            }
            result.mapValues { (_, deferred) -> deferred.await() }
        }
        Log.d("Author", "byOlid" + byOlid.toString())

        val merged = googleNames.mapIndexed { index, googleName ->
            val olid = orderedOlids.getOrNull(index)
            if (olid.isNullOrBlank()) {
                Authors(googleName, "", "")
            } else {
                val dto = byOlid[olid]
                val displayName = if (preferOlDisplayNames) {
                    dto?.name?.trim()?.takeIf { it.isNotEmpty() } ?: googleName
                } else {
                    googleName
                }
                dto.toAuthors(displayName)
            }
        }
        return Result.success(merged)
    }

    private fun AuthorDto?.toAuthors(displayName: String): Authors {
        if (this == null) return Authors(displayName, "", "")
        val photo = photos?.firstOrNull()?.let { id ->
            "https://covers.openlibrary.org/a/id/$id-M.jpg?default=false"
        }.orEmpty()
        return Authors(
            authorName = displayName,
            authorPhotoUrl = photo,
            authorBio = bio.toBioText(),
        )
    }

    private suspend fun fetchOlidsFromIsbn(isbn: String): List<String> {
        val response = api.getEditionByIsbn(isbn)
        if (!response.isSuccessful) return emptyList()
        val edition = response.body() ?: return emptyList()
        return edition.authors.orEmpty().mapNotNull { it.key?.toOpenLibraryAuthorOlid() }
    }

    private suspend fun fetchOlidsFromSearch(title: String, authorsLine: String): List<String> {
        val q = buildString {
            append("title:${title.trim()}".replace(' ', '+'))

            val authorsStr = authorsLine.split(" and ").joinToString()
            val first = authorsStr.split(',', '&').firstOrNull()?.trim().orEmpty()
            if (first.isNotEmpty()) {
                append("+author:${first.replace(' ', '+')}")
            }
        }.trim()
        if (q.isBlank()) return emptyList()

        val response = api.searchBooks(query = q, limit = 1)
        if (!response.isSuccessful) return emptyList()
        val doc = response.body()?.docs?.firstOrNull() ?: return emptyList()
        return doc.authorKeys.orEmpty().mapNotNull { it.toOpenLibraryAuthorOlid() }
    }

    fun String?.normalizeIsbnForOl(): String? {
        val raw = this ?: return null
        if (raw == "—" || raw.isBlank()) return null
        val digits = raw.filter { it.isDigit() }
        return when (digits.length) {
            10, 13 -> digits
            else -> null
        }
    }

    fun String.toOpenLibraryAuthorOlid(): String? {
        val s = removePrefix("/authors/").trim()
        return s.takeIf { it.isNotEmpty() }
    }
}
