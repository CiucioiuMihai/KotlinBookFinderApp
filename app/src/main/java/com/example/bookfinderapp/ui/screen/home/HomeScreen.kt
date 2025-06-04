package com.example.bookfinderapp.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookfinderapp.viewmodel.BookViewModel
import com.example.bookfinderapp.data.model.Book
import com.example.bookfinderapp.ui.components.BookItem

@Composable
fun HomeScreen(
    viewModel: BookViewModel = hiltViewModel(),
    onBookClick: (Book) -> Unit = {}
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load some trending books when the screen is first displayed
    LaunchedEffect(key1 = Unit) {
        // Use some popular search terms to simulate trending books
        viewModel.searchBooks("harry potter")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Trending Books",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (books.isEmpty()) {
                    Text(
                        text = "No books found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(books) { book ->
                            BookItem(book = book, onClick = { onBookClick(book) })
                        }
                    }
                }
            }
        }
    }
}
