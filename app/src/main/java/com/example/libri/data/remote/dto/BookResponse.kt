package com.example.libri.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("docs")
    val docs: List<BookDto>
)

data class BookDto(
    @SerializedName("key") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authors: List<String>?,
    @SerializedName("cover_i") val coverId: Int?,
    @SerializedName("first_publish_year") val year: Int?
)