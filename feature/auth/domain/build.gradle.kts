plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

apply(from = "$rootDir/ktlint.gradle.kts")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {

}
