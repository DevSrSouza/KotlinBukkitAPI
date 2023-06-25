enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "KotlinBukkitAPI"

include(":utility")
include(":architecture")
include(":menu")
include(":command-legacy")
include(":scoreboard-legacy")
include(":coroutines")
include(":extensions")
include(":serialization")
include(":exposed")

project(":extensions").name = "mc-extensions" // Gradle apparently does not accept modules with name "extensions"

plugins {
    id("com.gradle.enterprise") version("3.13")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}