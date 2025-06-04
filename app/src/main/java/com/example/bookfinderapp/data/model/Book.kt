package com.example.bookfinderapp.data.model

data class Book(
    val id: String = "", // Added default value
    val title: String = "", // Added default value
    val subtitle: String? = null,
    val authors: List<String> = emptyList(),
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val categories: List<String>? = null,
    val averageRating: Double? = null,
    val ratingsCount: Int? = null,
    val thumbnail: String? = null,
    val smallThumbnail: String? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val isbn10: String? = null,
    val isbn13: String? = null
)
