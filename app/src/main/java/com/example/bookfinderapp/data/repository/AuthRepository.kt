package com.example.bookfinderapp.data.repository

import com.example.bookfinderapp.data.firebase.FirebaseAuthSource
import com.google.firebase.auth.FirebaseUser

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(private val firebaseAuthSource: FirebaseAuthSource) {
    val currentUser: FirebaseUser?
        get() = firebaseAuthSource.currentUser

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            firebaseAuthSource.login(email, password)
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun register(email: String, password: String, name: String): AuthResult {
        return try {
            val user = firebaseAuthSource.register(email, password)
            // Optionally update display name here
            user?.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build())
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    fun logout() {
        firebaseAuthSource.logout()
    }
}
