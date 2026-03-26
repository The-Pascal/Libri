package com.example.libri.data.remote

import com.example.libri.data.remote.dto.google.BookVolume
import com.example.libri.data.remote.dto.google.GoogleBooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApi {

    // 1. searchBooks -> General search
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("orderBy") orderBy: String = "relevance",
        @Query("maxResults") maxResults: Int = 20,
        @Query("startIndex") startIndex: Int = 0,
        @Query("filter") filter: String? = null,
    ): Response<GoogleBooksResponse>

    // 2. getTrendingDaily -> Curated search for new/popular
    // SDE 3 Tip: Google doesn't have "trending," so we query for a broad subject sorted by newest
    @GET("volumes")
    suspend fun getTrendingBooks(
        @Query("q") query: String = "subject:fiction",
        @Query("orderBy") orderBy: String = "newest",
        @Query("maxResults") maxResults: Int = 15,
    ): Response<GoogleBooksResponse>

    // 3. getBooksByGenre -> Category search
    @GET("volumes")
    suspend fun getBooksByCategory(
        @Query("q") category: String, // Pass "subject:philosophy"
        @Query("maxResults") maxResults: Int = 15,
    ): Response<GoogleBooksResponse>

    // 4. getTrendingBooksByAuthor -> inauthor search
    @GET("volumes")
    suspend fun getBooksByAuthor(
        @Query("q") authorQuery: String, // Pass "inauthor:James Clear"
        @Query("orderBy") orderBy: String = "relevance",
        @Query("maxResults") maxResults: Int = 10,
    ): Response<GoogleBooksResponse>

    // 5. getBookDetails -> Specific Volume ID
    @GET("volumes/{volumeId}")
    suspend fun getBookDetails(
        @Path("volumeId") volumeId: String,
    ): Response<BookVolume>

    @GET("users/{userId}/bookshelves/{shelfId}/volumes")
    suspend fun getVolumesFromShelf(
        @Path("userId") userId: String,
        @Path("shelfId") shelfId: String,
        @Query("maxResults") maxResults: Int = 20,
        @Query("startIndex") startIndex: Int = 0
    ): Response<GoogleBooksResponse>
}