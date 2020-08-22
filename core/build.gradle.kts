repositories {
    maven("http://nexus.okkero.com/repository/maven-releases/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    api("com.okkero.skedule:skedule:1.2.6") {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation("org.bstats:bstats-bukkit:1.7")

    testImplementation(kotlin("stdlib"))
    testImplementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.junit.platform:junit-platform-launcher:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.3.2")

    testImplementation("org.mockito:mockito-core:2.24.0")
}