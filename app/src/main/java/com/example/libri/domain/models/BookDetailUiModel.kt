package com.example.libri.domain.models

data class BookDetailUiModel(
    val title: String,                // "The Architecture of Stillness"
    val authors: List<Authors>,
    val subjects: List<String>?,       // ["Philosophy", "Architecture", "Mindfulness"]
    val stats: BookStats,             // Pages: 324, Language: English, etc.
    val description: String,          // "The Architecture of Stillness is..."
    val authorBio: String,            // "Award-winning author..."
    val authorPhotoUrl: String,
    val coverUrl: String
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