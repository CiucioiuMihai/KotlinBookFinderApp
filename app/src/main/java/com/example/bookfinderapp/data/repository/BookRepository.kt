package com.example.bookfinderapp.data.repository

import com.example.bookfinderapp.data.model.Book
import com.example.bookfinderapp.data.network.GoogleBooksRemoteDataSource
import com.example.bookfinderapp.data.firebase.FirebaseBookDataSource
import com.example.bookfinderapp.data.firebase.FirebaseReviewSource
import com.example.bookfinderapp.data.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(
    private val remoteDataSource: GoogleBooksRemoteDataSource,
    private val localDataSource: FirebaseBookDataSource,
    private val reviewSource: FirebaseReviewSource? = null
) {
    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        val response = remoteDataSource.searchBooks(query)
        if (response.isSuccessful) {
            response.body()?.items?.mapNotNull { item ->
                // Map GoogleBookItem to Book (implement your mapping logic)
                item.toBook()
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getBookById(volumeId: String): Book? = withContext(Dispatchers.IO) {
        val response = remoteDataSource.getBookById(volumeId)
        if (response.isSuccessful) {
            response.body()?.toBook()
        } else {
            null
        }
    }

    suspend fun getFavoriteBooks(userId: String): List<Book> = withContext(Dispatchers.IO) {
        localDataSource.getFavoriteBooks(userId)
    }

    suspend fun addBookToFavorites(userId: String, book: Book) = withContext(Dispatchers.IO) {
        localDataSource.addBookToFavorites(userId, book)
    }

    suspend fun removeBookFromFavorites(userId: String, bookId: String) = withContext(Dispatchers.IO) {
        localDataSource.removeBookFromFavorites(userId, bookId)
    }

    suspend fun getReadingList(userId: String): List<Book> = withContext(Dispatchers.IO) {
        localDataSource.getReadingList(userId)
    }

    suspend fun addBookToReadingList(userId: String, book: Book) = withContext(Dispatchers.IO) {
        localDataSource.addBookToReadingList(userId, book)
    }

    suspend fun removeBookFromReadingList(userId: String, bookId: String) = withContext(Dispatchers.IO) {
        localDataSource.removeBookFromReadingList(userId, bookId)
    }

    suspend fun addReview(review: Review) {
        reviewSource?.addReview(review)
    }

    suspend fun getReviewsForBook(bookId: String): List<Review> {
        return reviewSource?.getReviewsForBook(bookId) ?: emptyList()
    }
}

// Extension function to map GoogleBookItem to Book
private fun com.example.bookfinderapp.data.network.GoogleBookItem.toBook(): Book {
    val vi = this.volumeInfo
    val isbn10 = vi.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier
    val isbn13 = vi.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier
    return Book(
        id = this.id,
        title = vi.title ?: "",
        subtitle = vi.subtitle,
        authors = vi.authors ?: listOf("Unknown Author"),
        publisher = vi.publisher,
        publishedDate = vi.publishedDate,
        description = vi.description,
        pageCount = vi.pageCount,
        categories = vi.categories,
        averageRating = vi.averageRating,
        ratingsCount = vi.ratingsCount,
        thumbnail = vi.imageLinks?.thumbnail,
        smallThumbnail = vi.imageLinks?.smallThumbnail,
        language = vi.language,
        previewLink = vi.previewLink,
        infoLink = vi.infoLink,
        isbn10 = isbn10,
        isbn13 = isbn13
    )
}
