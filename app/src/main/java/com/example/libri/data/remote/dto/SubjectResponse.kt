package com.example.libri.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SubjectResponse(
    @SerializedName("name")
    val subjectName: String? = null,

    @SerializedName("work_count")
    val workCount: Int? = null,

    @SerializedName("works")
    val works: List<SubjectWorkDto>? = null
)

data class SubjectWorkDto(
    @SerializedName("key")
    val key: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("authors")
    val authors: List<SubjectAuthorDto>? = null,

    @SerializedName("cover_id")
    val coverId: Int? = null,

    @SerializedName("first_publish_year")
    val firstPublishYear: Int? = null
)

data class SubjectAuthorDto(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("key")
    val key: String? = null
)