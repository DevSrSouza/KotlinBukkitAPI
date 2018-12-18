fun RepositoryHandler.maven(name: String, url: String) {
    maven {
        this.name = name
        this.url = uri(url)
    }
}

repositories {
    maven("vault-repo", "http://nexus.hc.to/content/repositories/pub_releases")
    maven("placeholderapi", "http://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("mvdw-software", "http://repo.mvdw-software.be/content/groups/public/")
    maven("direct-from-github", "https://jitpack.io")
    maven("WorldEdit", "http://maven.sk89q.com/repo/")
    maven("ViaVersion", "https://repo.viaversion.com/")
    maven("perfectdreams", "https://repo.perfectdreams.net/")
}

(rootProject.ext["softPlugins"] as MutableList<String>).addAll(
        listOf("Vault", "PlaceholderAPI", "MVdWPlaceholderAPI", "ActionBarAPI",
                "TitleAPI", "WorldEdit", "ViaVersion", "BossBarAPI", "HologramAPI")
)

dependencies {
    compileOnly(project(":core"))

    // plugins
    compileOnly("net.milkbowl.vault:VaultAPI:1.6")
    compileOnly("me.clip:placeholderapi:2.8.5")
    compileOnly("be.maximvdw:MVdWPlaceholderAPI:2.5.2-SNAPSHOT") {
        exclude("org.spigotmc", "spigot")
    }
    compileOnly("com.github.ConnorLinfoot:ActionBarAPI:d60c2aedb9")
    compileOnly("com.github.ConnorLinfoot:TitleAPI:1.7.6")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.5")
    compileOnly("us.myles:viaversion-common:1.4.1")
    compileOnly("org.inventivetalent:bossbarapi:2.4.1") {
        exclude("org.inventivetalent.packetlistener", "api")
        exclude("org.mcstats.bukkit", "metrics-lite")
    }
    compileOnly("org.inventivetalent:hologramapi:1.4.0") {
        exclude("org.inventivetalent.packetlistener", "api")
    }
}