plugins {
    kotlin("plugin.serialization") version Dep.Versions.kotlin
}

dependencies {
    implementation(Dep.bstats)

    baseDependencies().forEach { pdm(it) }
    coreDependencies().forEach { pdm(it, excludeKotlin) }

    testImplementation(kotlin("stdlib"))
    testImplementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.junit.platform:junit-platform-launcher:1.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.7.1")

    testImplementation("org.mockito:mockito-core:3.9.0")
}