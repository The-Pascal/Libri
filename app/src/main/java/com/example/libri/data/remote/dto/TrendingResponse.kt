package com.example.libri.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrendingResponse(
    @SerializedName("query")
    val query: String? = null,
    @SerializedName("works")
    val works: List<TrendingWork>? = null,
    @SerializedName("days")
    val days: Int? = null
)

data class TrendingWork(
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("author_name")
    val authorNames: List<String>? = null,
    @SerializedName("cover_i")
    val coverId: Int? = null,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int? = null
)