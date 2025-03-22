plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Correct format

}

android {
    namespace = "com.example.quicknotesapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quicknotesapp"
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
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation("com.google.android.material:material:1.11.0") // Updated Material
    implementation(libs.androidx.activity)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Updated ConstraintLayout
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.recyclerview:recyclerview:1.3.2") // Updated RecyclerView
    implementation("androidx.preference:preference-ktx:1.2.0")

    // Room (Updated)
    var room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    // Lifecycle (Matching Versions)
    var lifecycle_version = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("com.github.yukuku:ambilwarna:2.0.1")

    implementation ("com.google.cloud:google-cloud-speech:2.6.0") // Google Speech API
    implementation ("com.google.code.gson:gson:2.8.9") // JSON parsing
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.20.0")
}

