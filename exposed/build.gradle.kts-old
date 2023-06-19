plugins {
    kotlin("plugin.serialization") version Dep.Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    exposedDependencies().forEach { pdm(it, excludeKotlin) }
}