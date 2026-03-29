package com.example.libri.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("docs")
    val docs: List<BookDto>
)

data class BookDto(
    @SerializedName("key") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authors: List<String>?,
    /** Open Library author ids, e.g. `OL2647934A`. */
    @SerializedName("author_key") val authorKeys: List<String>? = null,
    @SerializedName("cover_i") val coverId: Int?,
    @SerializedName("first_publish_year") val year: Int?
)