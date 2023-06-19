plugins {
    alias(libs.plugins.ktlint) apply false
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId) apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}