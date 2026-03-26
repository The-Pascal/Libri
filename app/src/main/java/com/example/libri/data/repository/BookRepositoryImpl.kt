package com.example.libri.data.repository

import android.util.Log
import com.example.libri.data.local.AppDatabase
import com.example.libri.data.local.entity.toDomain
import com.example.libri.data.local.entity.toEntity
import com.example.libri.data.mapper.toDomain
import com.example.libri.data.mapper.toDomainBooks
import com.example.libri.data.remote.GoogleBooksApi
import com.example.libri.data.remote.GutendexApi
import com.example.libri.data.remote.NytBooksApi
import com.example.libri.domain.models.Book
import com.example.libri.domain.repository.BookRepository
import com.example.libri.utils.BestSellerList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BookRepositoryImpl(
    private val googleApi: GoogleBooksApi,
    private val nytBooksApi: NytBooksApi,
    private val gutendexApi: GutendexApi,
    private val database: AppDatabase,
) : BookRepository {
    override suspend fun searchBooks(query: String, sort: String?): Flow<Result<List<Book>>> {
        return flow {
            val response = try {
                googleApi.searchBooks(query, sort ?: "relevance")
            } catch (e: Exception) {
                emit(Result.failure(e))
                return@flow
            }

            val booksResponse = response.body()
            if (response.isSuccessful && booksResponse != null) {
                val booksList = booksResponse.toDomain()
                getFavoriteBooks().collect { favoriteBooks ->
                    val favoriteBooks = favoriteBooks.map { it.id }.toSet()
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

    override suspend fun getFreeTreasures(): Result<List<Book>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = gutendexApi.search(sort = "popular")

                val books = response.body()?.toDomainBooks() ?: emptyList()
                Result.success(books)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getTrendingBooks(): Result<Map<BestSellerList, List<Book>>> {
        return try {
            val response = nytBooksApi.getFullOverview()
            if (response.isSuccessful) {
                val result = response.body()?.toDomain() ?: throw Exception("Null response received")
                Result.success(result)
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
            val response = googleApi.getBooksByCategory(genre)
            if (response.isSuccessful) {
                val result = response.body()?.toDomain() ?: throw Exception("Null response received")
                Result.success(result)
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
            val response = googleApi.getBooksByAuthor(authorOlid)
            if (response.isSuccessful) {
                val result = response.body()?.toDomain() ?: throw Exception("Null response received")
                Result.success(result)
            } else {
                Result.failure(Exception("API Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingBooksByAuthor: Exception", e)
            Result.failure(e)
        }
    }


//    override suspend fun getBookDetails(bookId: String): Flow<Result<BookDetail>> = flow {
//        val worksResponse = try {
//            api.getBookDetails(bookId)
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//            return@flow
//        }
//
//        val works: WorkDto? = worksResponse.body()
//        if (worksResponse.isSuccessful && works != null) {
//            val authorKeys = works.getAuthorKeys()
//            val authorResults = mutableListOf<AuthorDto>()
//            var workEditionDto: EditionDto? = null
//            coroutineScope {
//                authorKeys?.map { authorId ->
//                    launch {
//                        getAuthorDetails(authorId).onSuccess {
//                            authorResults.add(it)
//                        }
//                    }
//                }?.joinAll()
//
//                getWorkEditions(works.getWorkId()).onSuccess {
//                    workEditionDto = it
//                }
//            }
//
//            getFavoriteBooks().collect { favoriteBooks ->
//                val favoriteBooks = favoriteBooks.map { it.id }.toSet()
//
//            }
//
//        }
//
//    }
//
//    private suspend fun getAuthorDetails(authorId: String): Result<AuthorDto> {
//        return try {
//            val result = api.getAuthorDetails(authorId)
//            if (result.isSuccessful) {
//                Result.success(result.body()!!)
//            } else {
//                Result.failure(Exception("API Error"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    private suspend fun getWorkEditions(workId: String): Result<EditionDto> {
//        return try {
//            val result = api.getWorkEditions(workId)
//            if (result.isSuccessful) {
//                Result.success(result.body()!!)
//            } else {
//                Result.failure(Exception("API Error"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }


    companion object {
        private const val TAG = "BookRepositoryImpl"
    }
}