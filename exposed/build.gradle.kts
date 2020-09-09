plugins {
    kotlin("plugin.serialization") version Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    exposedDependencies().forEach { pdm(it, excludeKotlin) }
}