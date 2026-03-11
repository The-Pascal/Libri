package com.example.libri.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    private const val BASE_URL = "https://openlibrary.org"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val openLibraryApi by lazy {
        retrofit.create(OpenLibraryApi::class.java)
    }
}