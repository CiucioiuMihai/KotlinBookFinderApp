package com.example.bookfinderapp.ui.screen.search

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookfinderapp.viewmodel.BookViewModel
import com.example.bookfinderapp.data.model.Book
import com.example.bookfinderapp.ui.components.BookItem

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: BookViewModel = hiltViewModel(),
    onBookClick: (Book) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Suggestions for popular searches
    val suggestions = listOf("Harry Potter", "Lord of the Rings", "Science Fiction", "Stephen King", "Romance")

    fun performSearch() {
        if (query.isNotBlank()) {
            viewModel.searchBooks(query)
            keyboardController?.hide()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search bar
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search for books") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { performSearch() }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Search button
        Button(
            onClick = { performSearch() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search")
        }

        // Suggestions chips (horizontal scrollable row)
        if (books.isEmpty() && query.isEmpty() && !isLoading) {
            Text(
                text = "Suggestions:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = {
                            query = suggestion
                            performSearch()
                        },
                        label = { Text(suggestion) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                books.isEmpty() && query.isNotBlank() -> Text(
                    text = "No results found for \"$query\".",
                    modifier = Modifier.align(Alignment.Center)
                )
                books.isNotEmpty() -> {
                    Column {
                        Text(
                            text = "Results for \"$query\":",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(books) { book ->
                                BookItem(book = book, onClick = { onBookClick(book) })
                            }
                        }
                    }
                }
                else -> {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search for books by title, author, or category.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
