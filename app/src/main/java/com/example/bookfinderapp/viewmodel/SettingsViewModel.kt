package com.example.bookfinderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfinderapp.data.preferences.UserPreferences
import com.example.bookfinderapp.data.repository.UserStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val userStateRepository: UserStateRepository
) : ViewModel() {

    // Get the dark mode setting as a StateFlow from UserPreferences for the current user
    val isDarkMode: StateFlow<Boolean> = userStateRepository.userIdFlow
        .flatMapLatest { userId ->
            userPreferences.isDarkModeEnabledForUser(userId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false // Default to light mode
        )

    // Update dark mode setting in UserPreferences for the current user
    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val userId = userStateRepository.userIdFlow.value
            userPreferences.setDarkMode(userId, enabled)
        }
    }
}
