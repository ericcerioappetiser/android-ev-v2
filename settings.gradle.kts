pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.google.com/") }
    }
    versionCatalogs {
        create("testLibs") {
            from(files("testing-dependencies.toml"))
        }
    }
}

rootProject.name = "AndroidecvAndroid"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core")
include(":core:network")
include(":core:database")
include(":core:common")
include(":core:common-compose")
include(":core:common-android")
include(":core:domain")
include(":core:test:local")
include(":core:user:local")
include(":core:user:network")
include(":core:mediapicker")

include(":feature")

include(":feature:auth")
include(":feature:auth:network")
include(":feature:auth:data")
include(":feature:auth:domain")
include(":feature:auth:ui")
include(":feature:onboarding")
include(":feature:onboarding:data")
include(":feature:onboarding:ui")
include(":feature:walkthrough:ui")
include(":feature:profile:ui")
include(":feature:profile:domain")
include(":feature:permission-provider:ui")
include(":feature:feeds:data")
include(":feature:feeds:domain")
include(":feature:feeds:ui")
include(":feature:feeds:network")
include(":feature:report:network")
include(":feature:report:local")
include(":feature:report:data")
include(":feature:report:ui")
include(":feature:search")
include(":feature:search:ui")
include(":feature:search:network")
include(":feature:search:data")
