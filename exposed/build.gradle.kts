plugins {
    kotlin("plugin.serialization") version Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    exposedDependencies().forEach { compileOnly(it, excludeKotlin) }
}