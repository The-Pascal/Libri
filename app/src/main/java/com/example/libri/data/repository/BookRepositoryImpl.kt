package com.example.libri.data.repository

import android.util.Log
import com.example.libri.data.local.AppDatabase
import com.example.libri.data.local.entity.toDomain
import com.example.libri.data.local.entity.toEntity
import com.example.libri.data.mapper.toDomain
import com.example.libri.data.mapper.toDomainModel
import com.example.libri.data.remote.OpenLibraryApi
import com.example.libri.domain.models.Book
import com.example.libri.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BookRepositoryImpl(
    private val api: OpenLibraryApi,
    private val database: AppDatabase,
) : BookRepository {
    override suspend fun searchBooks(query: String, sort: String?): Flow<Result<List<Book>>> {
        return flow {
            val response = try {
                api.searchBooks(query, sort)
            } catch (e: Exception) {
                emit(Result.failure(e))
                return@flow
            }

            if (response.isSuccessful) {
                val booksList = response.body()?.docs?.map { it.toDomain() } ?: emptyList()
                getFavoriteBooks().collect {
                    val favoriteBooks = it.map { it.id }.toSet()
                    val newBookList = booksList.map { book ->
                        book.copy(isBookmarked = favoriteBooks.contains(book.id))
                    }
                    emit(Result.success(newBookList))
                }
            } else {
                emit(Result.failure(Exception("API Error")))
            }
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> =
        database.bookDao().getBookmarked().map { it.toDomain() }

    override suspend fun insertBook(book: Book) {
        withContext(Dispatchers.IO) {
            database.bookDao().insertBook(book.toEntity())
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

    override suspend fun getTrendingBooksByAuthor(authorOlid: String): Result<List<Book>> {
        return try {
            val response = api.getTrendingBooksByAuthor(authorOlid)
            if (response.isSuccessful) {
                Result.success(response.body()?.docs?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingBooksByAuthor: Exception", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "BookRepositoryImpl"
    }
}