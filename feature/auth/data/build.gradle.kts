plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

apply(from = "$rootDir/secret.gradle.kts")
apply(from = "$rootDir/ktlint.gradle.kts")

android {
    namespace = "com.appetiser.feature.auth.data"
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
    }
}

dependencies {
    implementation(projects.feature.auth.network)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(projects.core.user.network)
    implementation(libs.retrofit)

    implementation(libs.kotlinx.serialization.json)

    api(projects.core.network)
    api(projects.core.user.local)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    // Testing
    testImplementation(testLibs.junit)
    testImplementation(testLibs.mockk)
    testImplementation(testLibs.mockk.jvm)
    testImplementation(testLibs.google.truth)
    testImplementation(testLibs.coroutines.test)
}

ksp {
    arg("ksp.correctErrorTypes", "true")
}
