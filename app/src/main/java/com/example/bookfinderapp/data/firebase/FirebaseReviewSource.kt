package com.example.bookfinderapp.data.firebase

import com.example.bookfinderapp.data.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseReviewSource(private val db: FirebaseFirestore) {
    private val reviewsCollection get() = db.collection("reviews")

    suspend fun addReview(review: Review) {
        val data = hashMapOf(
            "bookId" to review.bookId,
            "userId" to review.userId,
            "userName" to review.userName,
            "rating" to review.rating,
            "review" to review.review,
            "createdAt" to review.createdAt,
            "updatedAt" to review.updatedAt
        )
        reviewsCollection.add(data).await()
    }

    suspend fun getReviewsForBook(bookId: String): List<Review> {
        val snapshot = reviewsCollection.whereEqualTo("bookId", bookId).get().await()
        return snapshot.documents.mapNotNull { doc ->
            val data = doc.data ?: return@mapNotNull null
            Review(
                id = doc.id,
                bookId = data["bookId"] as? String ?: "",
                userId = data["userId"] as? String ?: "",
                userName = data["userName"] as? String,
                rating = (data["rating"] as? Long)?.toInt() ?: 0,
                review = data["review"] as? String,
                createdAt = (data["createdAt"] as? Long) ?: 0L,
                updatedAt = (data["updatedAt"] as? Long)
            )
        }
    }
}
