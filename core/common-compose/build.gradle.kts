plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
}

apply(from = "$rootDir/ktlint.gradle.kts")

android {
    namespace = "com.appetiser.core.common.compose"
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
        debug {
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(projects.core.common)
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    implementation(libs.androidx.constraintlayout.compose)

    api(platform(libs.coil.bom))
    api(libs.coil.compose)
    api(libs.coil)

    // Dagger-Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    api(libs.hilt.android)
    // ViewModel & LiveData
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.livedata)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.navigation.compose)
    ksp(libs.androidx.lifecycle.compiler)
    implementation(projects.core.commonAndroid)
}

ksp {
    arg("ksp.correctErrorTypes", "true")
}
