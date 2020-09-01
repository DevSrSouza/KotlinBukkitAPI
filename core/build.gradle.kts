plugins {
    kotlin("plugin.serialization") version Versions.kotlin
    id("me.bristermitten.pdm")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:${Versions.bstats}")
    compileOnly(project(":libraries-embedded"))

    testImplementation(kotlin("stdlib"))
    testImplementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.junit.platform:junit-platform-launcher:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.3.2")

    testImplementation("org.mockito:mockito-core:2.24.0")
}