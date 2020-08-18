plugins {
    kotlin("plugin.serialization") version "1.4.0"
}

dependencies {
    compileOnly(project(":core"))//, configuration = "shadow"))

    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    api("com.charleskorn.kaml:kaml:0.19.0")
    //implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:0.1.0")
}