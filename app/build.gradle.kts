plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.bookfinderapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookfinderapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // DataStore dependencies
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

    // Jetpack Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Material3 (if not already present)
    implementation("androidx.compose.material3:material3:1.2.1")
    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.0")
    // Retrofit for Google Books API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")
    // Firebase BOM (recommended)
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    // Optional: Firebase Analytics (if you want analytics)
    // implementation("com.google.firebase:firebase-analytics-ktx")
    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    // Hilt navigation-compose for Compose integration
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // Fix for Hilt/JavaPoet error
    implementation("com.squareup:javapoet:1.13.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

configurations.all {
    resolutionStrategy {
        force("com.squareup:javapoet:1.13.0")
    }
}

// Apply Google Services plugin
apply(plugin = "com.google.gms.google-services")

hilt {
    enableAggregatingTask = false
}
