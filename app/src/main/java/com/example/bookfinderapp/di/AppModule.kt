package com.example.bookfinderapp.di

import android.app.Application
import com.example.bookfinderapp.data.firebase.FirebaseAuthSource
import com.example.bookfinderapp.data.firebase.FirebaseBookDataSource
import com.example.bookfinderapp.data.firebase.FirebaseReviewSource
import com.example.bookfinderapp.data.network.GoogleBooksApiService
import com.example.bookfinderapp.data.network.GoogleBooksRemoteDataSource
import com.example.bookfinderapp.data.repository.AuthRepository
import com.example.bookfinderapp.data.repository.BookRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGoogleBooksApiService(): GoogleBooksApiService =
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApiService::class.java)

    @Provides
    @Singleton
    fun provideGoogleBooksRemoteDataSource(api: GoogleBooksApiService): GoogleBooksRemoteDataSource =
        GoogleBooksRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseBookDataSource(db: FirebaseFirestore): FirebaseBookDataSource =
        FirebaseBookDataSource(db)

    @Provides
    @Singleton
    fun provideFirebaseReviewSource(db: FirebaseFirestore): FirebaseReviewSource =
        FirebaseReviewSource(db)

    @Provides
    @Singleton
    fun provideBookRepository(
        remote: GoogleBooksRemoteDataSource,
        local: FirebaseBookDataSource,
        reviewSource: FirebaseReviewSource
    ): BookRepository = BookRepository(remote, local, reviewSource)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuthSource(firebaseAuth: FirebaseAuth): FirebaseAuthSource =
        FirebaseAuthSource(firebaseAuth)

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuthSource: FirebaseAuthSource): AuthRepository =
        AuthRepository(firebaseAuthSource)
}
