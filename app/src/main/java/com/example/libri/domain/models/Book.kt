package com.example.libri.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val coverUrl: String?,
    val publishYear: String,
    val isBookmarked: Boolean = false
)