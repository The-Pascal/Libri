package com.example.libri.data.repository

import android.util.Log
import com.example.libri.data.mapper.toDomain
import com.example.libri.data.mapper.toDomainModel
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

    override suspend fun getTrendingBooks(): Result<List<Book>> {
        return try {
            val response = api.getTrendingDaily()
            if (response.isSuccessful) {
                Result.success(response.body()?.works?.map { it.toDomainModel() } ?: emptyList())
            } else {
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingBooks: Exception", e)
            Result.failure(e)
        }
    }

    override suspend fun getBooksByGenre(genre: String): Result<List<Book>> {
        return try {
            val response = api.getBooksByGenre(genre)
            if (response.isSuccessful) {
                Result.success(response.body()?.works?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getBooksByGenre: Exception", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "BookRepositoryImpl"
    }
}