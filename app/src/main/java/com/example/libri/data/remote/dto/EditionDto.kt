package com.example.libri.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EditionDto(
    @SerializedName("number_of_pages") val numberOfPages: Int? = null,
    @SerializedName("languages") val languages: List<LanguageDto>? = null,
    @SerializedName("publish_date") val publishDate: String? = null,
    @SerializedName("isbn_13") val isbn13: String? = null,
    @SerializedName("isbn_10") val isbn10:List<String>? = null,
)

data class LanguageDto(
    @SerializedName("key") val key: String
)
