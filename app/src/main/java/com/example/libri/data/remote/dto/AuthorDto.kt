package com.example.libri.data.remote.dto

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: JsonElement? = null,
    @SerializedName("photos") val photos: List<Int>? = null
)
