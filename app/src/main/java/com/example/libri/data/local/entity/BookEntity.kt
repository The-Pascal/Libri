package com.example.libri.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.libri.domain.models.Book
import com.example.libri.utils.ApiType

@Entity
data class BookEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "authors")
    val authors: List<String>,
    @ColumnInfo(name = "cover_url")
    val coverUrl: String?,
    @ColumnInfo(name = "publish_year")
    val publishYear: String,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean = false,
    @ColumnInfo(name = "api_type")
    val apiType: String
)

fun BookEntity.toDomain() = Book(
    id = id,
    title = title,
    authors = authors,
    coverUrl = coverUrl,
    publishYear = publishYear,
    isBookmarked = isBookmarked,
    averageRating = null,
    ratingsCount = null,
    apiType = ApiType.valueOf(apiType)
)

fun List<BookEntity>.toDomain() = map { it.toDomain() }

fun Book.toEntity() = BookEntity(
    id = id,
    title = title,
    authors = authors,
    coverUrl = coverUrl,
    publishYear = publishYear,
    isBookmarked = isBookmarked,
    apiType = apiType.name
)
