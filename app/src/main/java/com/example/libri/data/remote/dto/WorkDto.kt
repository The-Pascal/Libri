package com.example.libri.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.json.JsonElement

data class WorkDto(
    @SerializedName("key") val key: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: JsonElement? = null,
    @SerializedName("covers") val covers: List<Int>? = null,
    @SerializedName("subject_places") val subjectPlaces: List<String>? = null,
    @SerializedName("subjects") val subjects: List<String>? = null,
    @SerializedName("first_publish_date") val firstPublishDate: String? = null,
    @SerializedName("authors") val authors: List<AuthorEntry>? = null
) {
    fun getAuthorKeys(): List<String>? {
        return authors?.map { it.author.key.removePrefix("/authors/") }
    }

    fun getWorkId(): String {
        return key.removePrefix("/works/")
    }
}

data class AuthorEntry(
    @SerializedName("author") val author: AuthorKey
)

data class AuthorKey(
    @SerializedName("key") val key: String
)