package com.example.libri.data.remote.dto.google

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse(
    @SerializedName("kind") val kind: String? = null,
    @SerializedName("totalItems") val totalItems: Int = 0,
    @SerializedName("items") val items: List<BookVolume>? = null
)

data class BookVolume(
    @SerializedName("id") val id: String,
    @SerializedName("etag") val eTag: String? = null,
    @SerializedName("selfLink") val selfLink: String? = null,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo? = null,
    @SerializedName("saleInfo") val saleInfo: SaleInfo? = null,
    @SerializedName("accessInfo") val accessInfo: AccessInfo? = null
)

data class VolumeInfo(
    @SerializedName("title") val title: String? = "Unknown Title",
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("authors") val authors: List<String>? = emptyList(),
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("publishedDate") val publishedDate: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("pageCount") val pageCount: Int? = null,
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("averageRating") val averageRating: Double? = null,
    @SerializedName("ratingsCount") val ratingsCount: Int? = null,
    @SerializedName("imageLinks") val imageLinks: ImageLinks? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("previewLink") val previewLink: String? = null,
    @SerializedName("infoLink") val infoLink: String? = null,
    @SerializedName("industryIdentifiers") val industryIdentifiers: List<IndustryIdentifier>? = null
)

data class IndustryIdentifier(
    @SerializedName("type") val type: String,
    @SerializedName("identifier") val identifier: String
)

data class ImageLinks(
    @SerializedName("smallThumbnail") val smallThumbnail: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("large") val large: String? = null // Only available in getBookDetails
)

data class SaleInfo(
    @SerializedName("country") val country: String? = null,
    @SerializedName("saleability") val saleability: String? = null,
    @SerializedName("isEbook") val isEbook: Boolean = false,
    @SerializedName("listPrice") val listPrice: Price? = null
)

data class Price(
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("currencyCode") val currencyCode: String? = null
)

data class AccessInfo(
    @SerializedName("webReaderLink") val webReaderLink: String? = null,
    @SerializedName("pdf") val pdf: FileAvailability? = null
)

data class FileAvailability(
    @SerializedName("isAvailable") val isAvailable: Boolean = false,
    @SerializedName("acsTokenLink") val acsTokenLink: String? = null
)
