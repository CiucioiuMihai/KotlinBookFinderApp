package com.example.bookfinderapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UserStateRepository acts as a central source of truth for user state information
 * that can be safely shared across different ViewModels without creating circular dependencies.
 */
@Singleton
class UserStateRepository @Inject constructor() {

    // User ID state
    private val _userIdFlow = MutableStateFlow<String?>(null)
    val userIdFlow: StateFlow<String?> = _userIdFlow.asStateFlow()

    // Username state
    private val _userNameFlow = MutableStateFlow<String?>(null)
    val userNameFlow: StateFlow<String?> = _userNameFlow.asStateFlow()

    // Add a state that triggers whenever the user changes to force theme recalculation
    private val _userChangeCounter = MutableStateFlow(0)
    val userChangeCounter: StateFlow<Int> = _userChangeCounter.asStateFlow()

    // Flag to control when user preferences should be applied
    private val _shouldApplyUserPreferences = MutableStateFlow(false)
    val shouldApplyUserPreferences: StateFlow<Boolean> = _shouldApplyUserPreferences.asStateFlow()

    /**
     * Updates the user state information
     */
    fun updateUserState(userId: String?, userName: String?) {
        val previousId = _userIdFlow.value
        _userIdFlow.value = userId
        _userNameFlow.value = userName

        // Increment counter when the user ID changes to force a theme reload
        if (previousId != userId) {
            _userChangeCounter.value += 1
        }

        // Enable user preferences after login
        if (userId != null) {
            _shouldApplyUserPreferences.value = true
        }
    }

    /**
     * Clears the user state information (e.g., on logout)
     */
    fun clearUserState() {
        _userIdFlow.value = null
        _userNameFlow.value = null
        _userChangeCounter.value += 1 // Also increment on logout
        _shouldApplyUserPreferences.value = false // Reset to light mode on logout
    }

    /**
     * Reset the theme application state (e.g., when app starts)
     */
    fun resetPreferencesState() {
        _shouldApplyUserPreferences.value = false
    }
}
