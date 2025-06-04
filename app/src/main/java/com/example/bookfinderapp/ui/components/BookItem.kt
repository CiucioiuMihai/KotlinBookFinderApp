package com.example.bookfinderapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookfinderapp.data.model.Book

@Composable
fun BookItem(
    book: Book,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.title)
            if (!book.authors.isNullOrEmpty()) {
                Text(text = book.authors.joinToString(", "))
            }
            if (!book.publisher.isNullOrBlank()) {
                Text(text = "Publisher: ${book.publisher}")
            }
            if (!book.publishedDate.isNullOrBlank()) {
                Text(text = "Published: ${book.publishedDate}")
            }
        }
    }
}
