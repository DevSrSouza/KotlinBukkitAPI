plugins {
    kotlin("plugin.serialization") version "1.4.0"
}

val exposed_version = "0.21.1"

dependencies {
    compileOnly(project(":core", configuration = "shadow"))

    compileOnly("org.jetbrains.exposed:exposed-core:$exposed_version")
    compileOnly("org.jetbrains.exposed:exposed-dao:$exposed_version")

    compileOnly("com.zaxxer:HikariCP:3.3.1")

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
}