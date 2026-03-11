package com.example.libri.data.repository

import com.example.libri.data.mapper.toDomain
import com.example.libri.data.remote.OpenLibraryApi
import com.example.libri.domain.models.Book
import com.example.libri.domain.repository.BookRepository

class BookRepositoryImpl(
    private val api: OpenLibraryApi,
) : BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>> {
        return try {
            val response = api.searchBooks(query)
            if (response.isSuccessful) {
                Result.success(response.body()?.docs?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}