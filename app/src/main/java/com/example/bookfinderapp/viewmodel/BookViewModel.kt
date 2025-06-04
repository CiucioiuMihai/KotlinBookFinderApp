package com.example.bookfinderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfinderapp.data.model.Book
import com.example.bookfinderapp.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _favorites = MutableStateFlow<List<Book>>(emptyList())
    val favorites: StateFlow<List<Book>> = _favorites.asStateFlow()

    private val _readingList = MutableStateFlow<List<Book>>(emptyList())
    val readingList: StateFlow<List<Book>> = _readingList.asStateFlow()

    private val _currentBook = MutableStateFlow<Book?>(null)
    val currentBook: StateFlow<Book?> = _currentBook.asStateFlow()

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _books.value = bookRepository.searchBooks(query)
            _isLoading.value = false
        }
    }

    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            _favorites.value = bookRepository.getFavoriteBooks(userId)
        }
    }

    fun loadReadingList(userId: String) {
        viewModelScope.launch {
            _readingList.value = bookRepository.getReadingList(userId)
        }
    }

    fun addBookToFavorites(userId: String, book: Book) {
        viewModelScope.launch {
            bookRepository.addBookToFavorites(userId, book)
            loadFavorites(userId)
        }
    }

    fun removeBookFromFavorites(userId: String, bookId: String) {
        viewModelScope.launch {
            bookRepository.removeBookFromFavorites(userId, bookId)
            loadFavorites(userId)
        }
    }

    fun addBookToReadingList(userId: String, book: Book) {
        viewModelScope.launch {
            bookRepository.addBookToReadingList(userId, book)
            loadReadingList(userId)
        }
    }

    fun removeBookFromReadingList(userId: String, bookId: String) {
        viewModelScope.launch {
            bookRepository.removeBookFromReadingList(userId, bookId)
            loadReadingList(userId)
        }
    }

    fun getBookById(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val book = bookRepository.getBookById(bookId)
            _currentBook.value = book
            _isLoading.value = false
        }
    }

    // Add more functions for details, etc. as needed
}
