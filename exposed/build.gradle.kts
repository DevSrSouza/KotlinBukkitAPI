plugins {
    kotlin("plugin.serialization") version Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    compileOnly(project(":libraries-embedded"))
}