plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("plugin.serialization") version libs.versions.kotlin

}

apply(from = "$rootDir/ktlint.gradle.kts")

android {
    namespace = "com.appetiser.core.common.android"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.kotlinx.serialization.json)
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.livedata)
    api(libs.androidx.viewmodel)
    api(libs.hilt.navigation.compose)
    api(libs.timber)
    api(libs.kotlinx.atomicfu)
    api(platform(libs.coil.bom))
    implementation(libs.paging)
    implementation(libs.androidx.paging.runtime)
    api(libs.coil)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.network.cacheControl)
}

ksp {
    arg("ksp.correctErrorTypes", "true")
}
