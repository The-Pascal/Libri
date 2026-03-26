package com.example.libri.data.remote

import com.example.libri.data.remote.dto.AuthorDto
import com.example.libri.data.remote.dto.EditionDto
import com.example.libri.data.remote.dto.SearchResponse
import com.example.libri.data.remote.dto.SubjectResponse
import com.example.libri.data.remote.dto.TrendingResponse
import com.example.libri.data.remote.dto.WorkDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("sort") sort: String? = null,
        @Query("limit") limit: Int = 20
    ): Response<SearchResponse>

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

    @GET("search.json")
    suspend fun getTrendingBooksByAuthor(
        @Query("author") authorOlid: String,
        @Query("sort") sort: String = "trending",
        @Query("limit") limit: Int = 10
    ): Response<SearchResponse>

    @GET("works/{workId}.json")
    suspend fun getBookDetails(
        @Path("workId") workId: String
    ): Response<WorkDto>

    @GET("authors/{authorId}.json")
    suspend fun getAuthorDetails(
        @Path("authorId") authorId: String
    ): Response<AuthorDto>

    @GET("works/{workId}/editions.json")
    suspend fun getWorkEditions(
        @Path("workId") workId: String
    ): Response<EditionDto>
}