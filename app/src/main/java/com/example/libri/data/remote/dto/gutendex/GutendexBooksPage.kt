package com.example.libri.data.remote.dto.gutendex

import com.google.gson.annotations.SerializedName

data class GutendexBooksPage(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<GutendexBookDto>? = null,
)

data class GutendexBookDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("authors") val authors: List<GutendexPersonDto>? = null,
    @SerializedName("summaries") val summaries: List<String>? = null,
    @SerializedName("editors") val editors: List<GutendexPersonDto>? = null,
    @SerializedName("translators") val translators: List<GutendexPersonDto>? = null,
    @SerializedName("subjects") val subjects: List<String>? = null,
    @SerializedName("bookshelves") val bookshelves: List<String>? = null,
    @SerializedName("languages") val languages: List<String>? = null,
    @SerializedName("copyright") val copyright: Boolean? = null,
    @SerializedName("media_type") val mediaType: String? = null,
    /** MIME type → URL (e.g. `image/jpeg` → Gutenberg cover). */
    @SerializedName("formats") val formats: Map<String, String>? = null,
    @SerializedName("download_count") val downloadCount: Int? = null,
)

data class GutendexPersonDto(
    @SerializedName("name") val name: String,
    @SerializedName("birth_year") val birthYear: Int? = null,
    @SerializedName("death_year") val deathYear: Int? = null,
)