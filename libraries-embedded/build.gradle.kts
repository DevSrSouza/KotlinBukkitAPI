repositories {
    maven("http://nexus.okkero.com/repository/maven-releases/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    // core module
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.corouties}")
    api("com.okkero.skedule:skedule:${Versions.skedule}", excludeKotlin)

    // exposed module
    api("org.jetbrains.exposed:exposed-core:${Versions.exposed}", excludeKotlin)
    api("org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}", excludeKotlin)
    api("org.jetbrains.exposed:exposed-java-time:${Versions.exposed}", excludeKotlin)
    api("org.jetbrains.exposed:exposed-dao:${Versions.exposed}", excludeKotlin)
    api("com.zaxxer:HikariCP:${Versions.hikari}")

    // serialization module
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}")
    api("com.charleskorn.kaml:kaml:${Versions.kaml}")
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        relocateKotlinBukkitAPI()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            shadow.component(this)
        }
    }
}