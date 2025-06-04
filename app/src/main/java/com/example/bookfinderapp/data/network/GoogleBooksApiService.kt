package com.example.bookfinderapp.data.network

import com.example.bookfinderapp.data.model.Book
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Google Books API response models (simplified for search)
data class GoogleBooksApiResponse(
    val kind: String?,
    val totalItems: Int?,
    val items: List<GoogleBookItem>?
)

data class GoogleBookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String?,
    val subtitle: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val industryIdentifiers: List<IndustryIdentifier>?,
    val pageCount: Int?,
    val categories: List<String>?,
    val averageRating: Double?,
    val ratingsCount: Int?,
    val imageLinks: ImageLinks?,
    val language: String?,
    val previewLink: String?,
    val infoLink: String?,
    val canonicalVolumeLink: String?
)

data class IndustryIdentifier(
    val type: String?,
    val identifier: String?
)

data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?,
    val small: String?,
    val medium: String?,
    val large: String?,
    val extraLarge: String?
)

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20
    ): Response<GoogleBooksApiResponse>

    @GET("volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") volumeId: String
    ): Response<GoogleBookItem>
}
