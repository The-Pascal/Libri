package com.example.libri.domain.repository

import com.example.libri.data.remote.GoogleBooksApi
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.data.repository.GoogleBooksVolumeResolver
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookDetails
import com.example.libri.utils.BestSellerList
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getTrendingBooks(): Result<Map<BestSellerList, List<Book>>>

    suspend fun getBooksByGenre(genre: String): Result<List<Book>>

    suspend fun getTrendingBooksByAuthor(authorOlid: String): Result<List<Book>>

    suspend fun searchBooks(query: String, sort: String? = null): Flow<Result<List<Book>>>

    fun getFavoriteBooks(): Flow<List<Book>>

    suspend fun insertBook(book: Book)

    suspend fun getFreeTreasures(): Result<List<Book>>

    suspend fun loadBookDetailsFromGoogle(bookDetails: BookRepositoryImpl.GetBookDetailsType): Result<BookDetails>
}