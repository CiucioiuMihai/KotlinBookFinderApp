package com.example.bookfinderapp.data.firebase

import com.example.bookfinderapp.data.model.Book
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseBookDataSource(private val db: FirebaseFirestore) {
    suspend fun getFavoriteBooks(userId: String): List<Book> {
        val snapshot = db.collection("users").document(userId).collection("favorites").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Book::class.java)
        }
    }

    suspend fun addBookToFavorites(userId: String, book: Book) {
        db.collection("users").document(userId).collection("favorites").document(book.id).set(book).await()
    }

    suspend fun removeBookFromFavorites(userId: String, bookId: String) {
        db.collection("users").document(userId).collection("favorites").document(bookId).delete().await()
    }

    suspend fun getReadingList(userId: String): List<Book> {
        val snapshot = db.collection("users").document(userId).collection("readingList").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Book::class.java)
        }
    }

    suspend fun addBookToReadingList(userId: String, book: Book) {
        db.collection("users").document(userId).collection("readingList").document(book.id).set(book).await()
    }

    suspend fun removeBookFromReadingList(userId: String, bookId: String) {
        db.collection("users").document(userId).collection("readingList").document(bookId).delete().await()
    }

    // Add more methods as needed for collections, etc.
}
