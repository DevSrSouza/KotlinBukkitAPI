plugins {
    kotlin("plugin.serialization") version Dep.Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    serializationDependencies().forEach { pdm(it, excludeKotlin) }
    //implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:0.1.0")
}