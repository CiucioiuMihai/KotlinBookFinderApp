package com.example.bookfinderapp

import android.app.Application
import com.example.bookfinderapp.data.repository.UserStateRepository
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BookFinderApp : Application() {

    @Inject
    lateinit var userStateRepository: UserStateRepository

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Reset theme preferences state on app start to force dark mode default
        userStateRepository.resetPreferencesState()
    }
}
