package com.example.libri.data.remote

import com.example.libri.data.remote.dto.nyt.OverviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NytBooksApi {

    @GET("lists/overview.json")
    suspend fun getFullOverview(
        @Query("published_date") date: String? = null
    ): Response<OverviewResponse>
}