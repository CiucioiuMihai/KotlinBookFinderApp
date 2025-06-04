package com.example.bookfinderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfinderapp.data.preferences.UserPreferences
import com.example.bookfinderapp.data.repository.UserStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val userStateRepository: UserStateRepository
) : ViewModel() {

    // Combine user ID flow with the user change counter to force recomposition when user changes
    private val userChangeFlow = userStateRepository.userIdFlow.combine(
        userStateRepository.userChangeCounter
    ) { userId, _ -> userId }

    // Expose the shouldApplyUserPreferences flow for the AppTheme component
    val shouldApplyUserTheme = userStateRepository.shouldApplyUserPreferences

    // Get the dark mode preference for the current user
    val isDarkMode: StateFlow<Boolean> = userChangeFlow
        .flatMapLatest { userId ->
            // When the user ID changes, get the dark mode preference for that user
            userPreferences.isDarkModeEnabledForUser(userId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false // This default is only used until the real preference is loaded
        )
}
