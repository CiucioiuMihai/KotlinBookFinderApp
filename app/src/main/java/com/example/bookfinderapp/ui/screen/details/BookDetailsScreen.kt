package com.example.bookfinderapp.ui.screen.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookfinderapp.data.model.Book
import com.example.bookfinderapp.ui.components.ReviewItem
import com.example.bookfinderapp.viewmodel.BookViewModel
import com.example.bookfinderapp.viewmodel.ReviewViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.bookfinderapp.data.model.Review
import com.example.bookfinderapp.viewmodel.AuthViewModel

@Composable
fun BookDetailsScreen(
    book: Book? = null,
    bookId: String? = null,
    onAddReview: () -> Unit = {},
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    bookViewModel: BookViewModel = hiltViewModel()
) {
    val currentBookId = book?.id ?: bookId

    // Load book details if only bookId is provided
    LaunchedEffect(currentBookId) {
        if (book == null && !bookId.isNullOrEmpty()) {
            bookViewModel.getBookById(bookId)
        }
    }

    val currentBook = if (book != null) {
        book
    } else {
        val bookState by bookViewModel.currentBook.collectAsState()
        bookState
    }

    val isLoading by bookViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    if (currentBook == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Book not found", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    val reviews by reviewViewModel.reviews.collectAsState()
    val userId = authViewModel.userId

    // Fetch reviews when this screen is shown
    LaunchedEffect(currentBook.id) {
        reviewViewModel.fetchReviews(currentBook.id)
    }

    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3f) }
    var showForm by remember { mutableStateOf(false) }

    // Track if book is in favorites or reading list (can be enhanced with actual checking)
    var isInFavorites by remember { mutableStateOf(false) }
    var isInReadingList by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Text(
                text = currentBook.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (!currentBook.authors.isNullOrEmpty()) {
                Text(
                    text = "By: ${currentBook.authors.joinToString(", ")}",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Add to favorites and reading list buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        userId?.let {
                            if (!isInFavorites) {
                                bookViewModel.addBookToFavorites(it, currentBook)
                            } else {
                                bookViewModel.removeBookFromFavorites(it, currentBook.id)
                            }
                            isInFavorites = !isInFavorites
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInFavorites)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isInFavorites)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isInFavorites) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (isInFavorites) "In Favorites" else "Add to Favorites")
                    }
                }

                Button(
                    onClick = {
                        userId?.let {
                            if (!isInReadingList) {
                                bookViewModel.addBookToReadingList(it, currentBook)
                            } else {
                                bookViewModel.removeBookFromReadingList(it, currentBook.id)
                            }
                            isInReadingList = !isInReadingList
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInReadingList)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isInReadingList)
                            MaterialTheme.colorScheme.onSecondary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, contentDescription = "Reading List")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (isInReadingList) "In Reading List" else "Add to Reading List")
                    }
                }
            }

            if (!currentBook.description.isNullOrBlank()) {
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
                Text(text = currentBook.description, modifier = Modifier.padding(bottom = 16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showForm = !showForm }) {
                Text(if (showForm) "Cancel" else "Add a Review")
            }
            if (showForm) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Your Rating: ${rating.toInt()}")
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 1f..5f,
                    steps = 3
                )
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Your review") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val newReview = Review(
                            bookId = currentBook.id,
                            userId = authViewModel.userId ?: "",
                            userName = authViewModel.userName ?: "Anonymous",
                            rating = rating.toInt(),
                            review = reviewText,
                            createdAt = System.currentTimeMillis()
                        )
                        reviewViewModel.addReview(newReview)
                        reviewText = ""
                        rating = 3f
                        showForm = false
                    },
                    modifier = Modifier.fillMaxWidth() // changed from align(Alignment.End)
                ) {
                    Text("Submit Review")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Reviews:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (reviews.isEmpty()) {
                Text(text = "No reviews yet.")
            }
        }
        if (reviews.isNotEmpty()) {
            items(reviews) { review ->
                ReviewItem(review)
            }
        }
    }
}
