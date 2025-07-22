plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

apply(from = "$rootDir/ktlint.gradle.kts")

android {
    namespace = "com.appetiser.core.database"
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
        debug {
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
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.domain)
    api(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    // EncryptedSharedPreferences
    api(libs.security.crypto)

    ksp(libs.hilt.compiler)

    api(libs.hilt.android)
    // Room
    api(libs.room.runtime)

    implementation(libs.kotlinx.serialization.json)

    // SQLCipher
    api(libs.sql.cipher)
    ksp(libs.room.compiler)
    api(libs.room.ktx)
    implementation(projects.core.common)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

ksp {
    arg("ksp.correctErrorTypes", "true")
}

