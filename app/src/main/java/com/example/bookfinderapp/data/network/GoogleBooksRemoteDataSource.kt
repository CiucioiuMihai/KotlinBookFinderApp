package com.example.bookfinderapp.data.network

import com.example.bookfinderapp.data.model.Book
import retrofit2.Response

class GoogleBooksRemoteDataSource(private val api: GoogleBooksApiService) {
    suspend fun searchBooks(query: String, maxResults: Int = 20): Response<GoogleBooksApiResponse> {
        return api.searchBooks(query, maxResults)
    }

    suspend fun getBookById(volumeId: String): Response<GoogleBookItem> {
        return api.getBookById(volumeId)
    }

    // Optionally, add mapping from GoogleBookItem to Book here
}

