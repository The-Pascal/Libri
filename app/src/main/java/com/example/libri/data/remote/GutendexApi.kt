package com.example.libri.data.remote

import com.example.libri.data.remote.dto.gutendex.GutendexBooksPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GutendexApi {

    @GET("/books/")
    suspend fun search(
        @Query("sort") sort: String
    ): Response<GutendexBooksPage>
}