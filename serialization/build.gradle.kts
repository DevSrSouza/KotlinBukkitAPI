plugins {
    kotlin("plugin.serialization") version Versions.kotlin
}

dependencies {
    compileOnly(project(":core"))

    compileOnly(project(":libraries-embedded"))
    //implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:0.1.0")
}