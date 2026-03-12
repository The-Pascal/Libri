package com.example.libri.domain.repository

import com.example.libri.domain.models.Book

interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>>

    suspend fun getTrendingBooks(): Result<List<Book>>

    suspend fun getBooksByGenre(genre: String): Result<List<Book>>
}