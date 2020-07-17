plugins {
    kotlin("plugin.serialization") version "1.3.71"
}

dependencies {
    compileOnly(project(":core"))//, configuration = "shadow"))

    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    compile("com.charleskorn.kaml:kaml:0.17.0")
    compile("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:0.1.0")
}