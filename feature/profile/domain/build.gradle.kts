plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

apply(from = "$rootDir/ktlint.gradle.kts")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {}
