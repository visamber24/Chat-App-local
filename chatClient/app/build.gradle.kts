plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.lazysloth.chatapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.lazysloth.chatapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    //Room
    val room_version = "3.0.1"

    implementation(libs.androidx.room3.runtime)
//    implementation(libs.androidx.room.ktx) no need it for room3

    ksp(libs.androidx.room3.compiler)
// coil
    implementation(libs.coil.compose)
    // icon
    implementation(libs.androidx.compose.material.icons.extended)
    // koin
    implementation(libs.androidx.koin.compose)
    implementation(libs.androidx.koin.android)
    implementation(libs.androidx.navigation.compose)
    // http client ktor
    val ktorVersion = "3.0.3" // Or latest stable version
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio) // CIO engine
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

//    implementation(libs.androidx.httpClient)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}