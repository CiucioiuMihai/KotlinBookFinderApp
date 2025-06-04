package com.example.bookfinderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfinderapp.data.repository.AuthRepository
import com.example.bookfinderapp.data.repository.AuthResult
import com.example.bookfinderapp.data.repository.UserStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userStateRepository: UserStateRepository
) : ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> = _authResult.asStateFlow()

    // Use the repository's user ID and name flows
    val userIdFlow = userStateRepository.userIdFlow

    // Get the user ID and name directly from the auth repository
    val userId: String?
        get() = authRepository.currentUser?.uid
    val userName: String?
        get() = authRepository.currentUser?.displayName

    // Update user info in the repository
    private fun updateUserState() {
        userStateRepository.updateUserState(userId, userName)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _authResult.value = result
            _isAuthenticated.value = result is AuthResult.Success
            updateUserState() // Update user state after login
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            val result = authRepository.register(email, password, name)
            _authResult.value = result
            _isAuthenticated.value = result is AuthResult.Success
            updateUserState() // Update user state after registration
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isAuthenticated.value = false
            userStateRepository.clearUserState() // Clear user state on logout
        }
    }

    // Initialize user state when ViewModel is created
    init {
        updateUserState()
    }
}
