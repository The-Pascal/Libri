package com.example.libri.data.remote

import com.example.libri.data.remote.dto.BookResponse
import com.example.libri.data.remote.dto.SubjectResponse
import com.example.libri.data.remote.dto.TrendingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): Response<BookResponse>

    @GET("trending/daily.json")
    suspend fun getTrendingDaily(
        @Query("limit") limit: Int = 15
    ): Response<TrendingResponse>

    @GET("subjects/{genre}.json")
    suspend fun getBooksByGenre(
        @Path("genre") genre: String,
        @Query("limit") limit: Int = 15,
        @Query("details") details: Boolean = true
    ): Response<SubjectResponse>
}