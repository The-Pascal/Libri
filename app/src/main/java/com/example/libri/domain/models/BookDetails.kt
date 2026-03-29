package com.example.libri.domain.models

data class BookDetails(
    val authors: List<Authors>,
    val tags: List<String>?,
    val stats: BookStats,
    val description: String,
)

data class BookStats(
    val pages: String,
    val languages: String,
    val isbn: String,
    val publishedDate: String
)

data class Authors(
    val authorName: String,
    val authorPhotoUrl: String,
    val authorBio: String
)