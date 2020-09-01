repositories {
    maven("http://nexus.okkero.com/repository/maven-releases/")
    maven("https://repo.codemc.org/repository/maven-public")
}

dependencies {
    baseDependencies().forEach { api(it) }
    coreDependencies().forEach { api(it, excludeKotlin) }
    exposedDependencies().forEach { api(it, excludeKotlin) }
    serializationDependencies().forEach { api(it, excludeKotlin) }
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