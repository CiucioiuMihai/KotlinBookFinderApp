package com.example.bookfinderapp.ui.screen.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookfinderapp.viewmodel.AuthViewModel
import com.example.bookfinderapp.viewmodel.BookViewModel
import com.example.bookfinderapp.data.model.Book
import com.example.bookfinderapp.ui.components.BookItem

@Composable
fun LibraryScreen(
    viewModel: BookViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onBookClick: (Book) -> Unit = {}
) {
    val userId = authViewModel.userId
    val favorites by viewModel.favorites.collectAsState()
    val readingList by viewModel.readingList.collectAsState()

    // Load favorites and reading list when userId changes
    androidx.compose.runtime.LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadFavorites(it)
            viewModel.loadReadingList(it)
        }
    }

    val tabTitles = listOf("Favorites", "Reading List")
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { setSelectedTab(index) },
                    text = { Text(title) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (selectedTab) {
            0 -> { // Favorites Tab
                if (favorites.isEmpty()) {
                    Text(text = "No favorite books.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(favorites) { book ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BookItem(book = book, onClick = { onBookClick(book) })
                                Spacer(modifier = Modifier.width(4.dp))
                                Button(onClick = {
                                    userId?.let { viewModel.removeBookFromFavorites(it, book.id) }
                                }) {
                                    Text("Remove from Favorites")
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Button(onClick = {
                                    userId?.let { viewModel.addBookToReadingList(it, book) }
                                }) {
                                    Text("Add to Reading List")
                                }
                            }
                        }
                    }
                }
            }
            1 -> { // Reading List Tab
                if (readingList.isEmpty()) {
                    Text(text = "No books in your reading list.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(readingList) { book ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BookItem(book = book, onClick = { onBookClick(book) })
                                Spacer(modifier = Modifier.width(4.dp))
                                Button(onClick = {
                                    userId?.let { viewModel.removeBookFromReadingList(it, book.id) }
                                }) {
                                    Text("Remove from Reading List")
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Button(onClick = {
                                    userId?.let { viewModel.addBookToFavorites(it, book) }
                                }) {
                                    Text("Add to Favorites")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
