package com.example.bookfinderapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Create a DataStore instance at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Define preference keys
    companion object {
        private const val DARK_MODE_PREFIX = "dark_mode_"

        // Create a unique key for each user's dark mode preference
        fun darkModeKeyForUser(userId: String?): Preferences.Key<Boolean> {
            val safeUserId = userId ?: "guest"
            return booleanPreferencesKey("${DARK_MODE_PREFIX}${safeUserId}")
        }
    }

    // Get the dark mode state for a specific user as a Flow
    fun isDarkModeEnabledForUser(userId: String?): Flow<Boolean> {
        val darkModeKey = darkModeKeyForUser(userId)
        return context.dataStore.data.map { preferences ->
            preferences[darkModeKey] ?: false // Default to light mode
        }
    }

    // Update the dark mode setting for a specific user
    suspend fun setDarkMode(userId: String?, enabled: Boolean) {
        val darkModeKey = darkModeKeyForUser(userId)
        context.dataStore.edit { preferences ->
            preferences[darkModeKey] = enabled
        }
    }
}
