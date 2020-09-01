plugins {
    kotlin("plugin.serialization") version Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    serializationDependencies().forEach { compileOnly(it, excludeKotlin) }
    //implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:0.1.0")
}