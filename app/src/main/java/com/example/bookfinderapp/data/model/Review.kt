package com.example.bookfinderapp.data.model

data class Review(
    val id: String? = null, // Firebase document ID
    val bookId: String,
    val userId: String,
    val userName: String? = null,
    val rating: Int, // 1-5
    val review: String? = null,
    val createdAt: Long, // Timestamp (epoch millis)
    val updatedAt: Long? = null // Timestamp (epoch millis)
)
