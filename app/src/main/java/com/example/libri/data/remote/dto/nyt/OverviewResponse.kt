package com.example.libri.data.remote.dto.nyt

import com.google.gson.annotations.SerializedName

data class OverviewResponse(
    @SerializedName("status") val status: String,
    @SerializedName("results") val results: OverviewResult
)

data class OverviewResult(
    @SerializedName("published_date") val publishedDate: String,
    @SerializedName("lists") val lists: List<NytList>
)

data class NytList(
    @SerializedName("list_id") val listId: Int,
    @SerializedName("list_name") val listName: String,
    @SerializedName("list_name_encoded") val listNameEncoded: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("books") val books: List<NytBook>
)

data class NytBook(
    @SerializedName("primary_isbn13") val isbn13: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("rank") val rank: Int,
    @SerializedName("book_image") val bookImage: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_date") val createdDate: String
)