plugins {
    id(libs.plugins.kotlinbukkitapi.build.get().pluginId) apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}