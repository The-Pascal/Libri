package com.example.libri.domain.repository

import com.example.libri.domain.models.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getTrendingBooks(): Result<List<Book>>

    suspend fun getBooksByGenre(genre: String): Result<List<Book>>

    suspend fun getTrendingBooksByAuthor(authorOlid: String): Result<List<Book>>

    suspend fun searchBooks(query: String, sort: String? = null): Flow<Result<List<Book>>>

    fun getFavoriteBooks(): Flow<List<Book>>

    suspend fun insertBook(book: Book)
}