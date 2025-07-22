plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

apply(from = "$rootDir/secret.gradle.kts")
apply(from = "$rootDir/ktlint.gradle.kts")

android {
    namespace = "com.appetiser.feature.auth.ui"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    // Feature Modules
    implementation(projects.core.commonCompose)
    implementation(projects.core.commonAndroid)
    implementation(projects.feature.auth.data)
    implementation(projects.feature.walkthrough.ui)
    implementation(projects.core.common)
    // Jetpack
    // https://developer.android.com/reference/androidx/packages
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Compose Destinations
    // https://github.com/raamcosta/compose-destinations
    implementation(libs.androidx.navigation.compose)

    // Material2
    // https://m2.material.io/develop/android/docs/getting-started
    implementation(libs.material)

    // Material3
    // https://developer.android.com/develop/ui/compose/designsystems/material3
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Lib Phone Number
    // https://github.com/google/libphonenumber
    implementation(libs.lib.phone.number)

    // Hilt
    // https://developer.android.com/training/dependency-injection/hilt-android
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(libs.kotlinx.serialization.json)

    // Network
    implementation(libs.retrofit)

    // Testing
    // https://developer.android.com/training/testing
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(testLibs.junit)
    testImplementation(testLibs.mockk)
    testImplementation(testLibs.mockk.jvm)
    testImplementation(testLibs.google.truth)
    testImplementation(testLibs.coroutines.test)
    androidTestImplementation(testLibs.androidx.test.runner)
    androidTestImplementation(testLibs.androidx.espresso)
    androidTestImplementation(testLibs.coroutines.test)
}

ksp {
    arg("ksp.correctErrorTypes", "true")
}
