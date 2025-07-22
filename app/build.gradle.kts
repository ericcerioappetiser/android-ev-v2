plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

apply(from = "$rootDir/ktlint.gradle.kts")
apply(from = "$rootDir/secret.gradle.kts")

val secretDebug: List<Pair<String, String>> by project.extra
val secretProd: List<Pair<String, String>> by project.extra
val stagingApi: List<Pair<String, String>> by project.extra
val releaseApi: List<Pair<String, String>> by project.extra
val googleDebug: List<Pair<String, String>> by project.extra
val googleProd: List<Pair<String, String>> by project.extra
val facebookDebug: List<Pair<String, String>> by project.extra
val facebookProd: List<Pair<String, String>> by project.extra
val stripeDebug: List<Pair<String, String>> by project.extra
val stripeProd: List<Pair<String, String>> by project.extra
val termsAndCondition: List<Pair<String, String>> by project.extra
val privacyPolicy: List<Pair<String, String>> by project.extra
val sentryDebug: List<Pair<String, String>> by project.extra
val sentryProd: List<Pair<String, String>> by project.extra

android {
    namespace = "com.appetiser.androidecv"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        named("debug").configure {
            storeFile = file("$rootDir/app/${secretDebug[0].second}")
            storePassword = secretDebug[1].second
            keyAlias = secretDebug[3].second
            keyPassword = secretDebug[2].second
        }
        create("release") {
            // Todo replace this with your release prod secret key store
            storeFile = file("$rootDir/app/${secretDebug[0].second}")
            storePassword = secretDebug[1].second
            keyAlias = secretDebug[3].second
            keyPassword = secretDebug[2].second
        }
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = Integer.parseInt(System.getenv("BUILD_NUMBER") ?: "1")
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            // TODO replace this with your release config
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            secretProd.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            releaseApi.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            googleProd.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            facebookProd.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            stripeProd.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            termsAndCondition.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            privacyPolicy.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            facebookProd.forEach { resValue("string", it.first, "\"${it.second}\"") }
            googleProd.forEach { resValue("string", it.first, "\"${it.second}\"") }
            sentryProd.forEach { resValue("string", it.first, "\"${it.second}\"") }
            manifestPlaceholders["appName"] = "@string/app_name"
        }
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            isDebuggable = true

            secretDebug.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            stagingApi.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            googleDebug.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            facebookDebug.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            stripeDebug.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            termsAndCondition.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }
            privacyPolicy.forEach { buildConfigField("String", it.first, "\"${it.second}\"") }

            facebookDebug.forEach { resValue("string", it.first, "\"${it.second}\"") }
            googleDebug.forEach { resValue("string", it.first, "\"${it.second}\"") }
            sentryDebug.forEach { resValue("string", it.first, "\"${it.second}\"") }
            manifestPlaceholders["appName"] = "@string/app_name"
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    // Modules
    // https://gitlab.com/appetiser.androidecv/android/modularized-skeleton
    implementation(projects.core.commonCompose)
    implementation(projects.core.commonAndroid)
    implementation(projects.core.network)
    implementation(projects.feature.onboarding.ui)
    implementation(projects.feature.auth.ui)
    implementation(projects.feature.auth.data)
    implementation(projects.feature.walkthrough.ui)
    implementation(projects.feature.profile.ui)
    implementation(projects.feature.report.ui)

    // Dagger-Hilt
    // https://developer.android.com/training/dependency-injection/hilt-android
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    // Android Jetpack
    // https://developer.android.com/reference/androidx/packages
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Compose
    // https://developer.android.com/develop/ui/compose/documentation
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Material
    // https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
    implementation(libs.androidx.compose.material3)

    // Testing
    // https://developer.android.com/training/testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

ksp {
    arg("ksp.correctErrorTypes", "true")
}

fun getVersionName(): String {
    val major = "${project.properties["app_major"]}"
    val minor = "${project.properties["app_minor"]}"
    val patch = "${project.properties["app_patch"]}"
    return "$major.$minor.$patch"
}
