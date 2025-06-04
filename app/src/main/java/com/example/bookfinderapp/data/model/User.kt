package com.example.bookfinderapp.data.model

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val favorites: List<String>? = null,      // Book IDs that the user has favorited
    val readingList: List<String>? = null,    // Book IDs on user's reading list
    val readHistory: List<String>? = null,    // Book IDs the user has read
    val reviews: Map<String, String>? = null, // Book IDs mapped to user's reviews
    val createdAt: Long? = null,              // Timestamp for when user was created
    val lastLoginAt: Long? = null             // Timestamp for user's last login
)
