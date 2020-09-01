repositories {
    maven("https://repo.perfectdreams.net/")

    for (repository in KotlinBukkitAPI.plugins.flatMap { it.repositories }.distinct()) {
        maven(repository)
    }
}

dependencies {
    compileOnly(project(":core"))

    for (dependency in KotlinBukkitAPI.plugins.flatMap { it.dependencies }) {
        compileOnly(dependency) {
            exclude("org.spigotmc", "spigot")
            exclude("org.inventivetalent.packetlistener", "api")
            exclude("org.mcstats.bukkit", "metrics-lite")
        }
    }
}