package com.example.bookfinderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfinderapp.data.model.Review
import com.example.bookfinderapp.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private var currentBookId: String? = null

    // Example: fetch reviews for a book (to be implemented)
    fun fetchReviews(bookId: String) {
        currentBookId = bookId
        viewModelScope.launch {
            _reviews.value = bookRepository.getReviewsForBook(bookId)
        }
    }

    // Example: add a review (to be implemented)
    fun addReview(review: Review) {
        viewModelScope.launch {
            bookRepository.addReview(review)
            // Refresh reviews after adding
            currentBookId?.let { fetchReviews(it) }
        }
    }
}
