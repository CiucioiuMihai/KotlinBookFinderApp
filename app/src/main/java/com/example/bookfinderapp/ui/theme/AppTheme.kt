package com.example.bookfinderapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookfinderapp.viewmodel.ThemeViewModel

@Composable
fun AppTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    // Always use dark mode by default, regardless of user preference
    // Only after login will the user's preference be considered
    val userTheme by themeViewModel.isDarkMode.collectAsState()
    val shouldApplyUserTheme by themeViewModel.shouldApplyUserTheme.collectAsState(initial = false)

    // Use dark mode as default, only apply user preference after login
    val isDarkMode = if (shouldApplyUserTheme) userTheme else true

    BookFinderAppTheme(
        darkTheme = isDarkMode,
        content = content
    )
}
