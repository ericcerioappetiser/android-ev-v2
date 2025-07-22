plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

apply(from = "$rootDir/ktlint.gradle.kts")
apply(from = "$rootDir/secret.gradle.kts")

val stagingApi: List<Pair<String, String>> by project.extra
val releaseApi: List<Pair<String, String>> by project.extra

android {
    namespace = "com.appetiser.core.network"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            releaseApi.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
        }
        debug {
            stagingApi.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
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
    api(projects.core.common)
    api(projects.core.domain)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.retrofit.scalars)
    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.kotlinx.serialization.json)

    // Dagger-Hilt
    ksp(libs.hilt.compiler)
    api(libs.hilt.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.paging)
    implementation(libs.androidx.paging.runtime)
    // Chucker
    debugApi(libs.chucker)
    releaseApi(libs.chucker.noop)

    // Testing
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
