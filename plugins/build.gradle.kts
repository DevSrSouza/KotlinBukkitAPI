bukkitPlugins {
    repository("vault-repo", "http://nexus.hc.to/content/repositories/pub_releases") {
        plugin("Vault", "net.milkbowl.vault:VaultAPI:1.6")
    }
    repository("placeholderapi", "http://repo.extendedclip.com/content/repositories/placeholderapi/") {
        plugin("PlaceholderAPI", "me.clip:placeholderapi:2.8.5")
    }
    repository("mvdw-software", "http://repo.mvdw-software.be/content/groups/public/") {
        plugin("MVdWPlaceholderAPI", "be.maximvdw:MVdWPlaceholderAPI:2.5.2-SNAPSHOT")
    }
    repository("direct-from-github", "https://jitpack.io") {
        plugin("ActionBarAPI", "com.github.ConnorLinfoot:ActionBarAPI:d60c2aedb9")
        plugin("TitleAPI", "com.github.ConnorLinfoot:TitleAPI:1.7.6")
    }
    repository("WorldEdit", "http://maven.sk89q.com/repo/") {
        plugin("WorldEdit", "com.sk89q.worldedit:worldedit-bukkit:6.1.5")
    }
    repository("ViaVersion", "https://repo.viaversion.com/") {
        plugin("ViaVersion", "us.myles:viaversion-common:1.4.1")
    }
    repository("inventive-repo", "https://repo.inventivetalent.org/content/groups/public/") {
        plugin("PacketListenerApi", "org.inventivetalent.packetlistener:api:3.7.1-SNAPSHOT")
        plugin("HologramAPI", "org.inventivetalent:hologramapi:1.6.0")
        plugin("BossBarAPI", "org.inventivetalent:bossbarapi:2.4.1")
    }
}

repositories {
    for (plugin in bukkitPlugins.plugins) {
        maven {
            name = plugin.repoName
            url = uri(plugin.repo)
        }
    }
}

dependencies {
    compileOnly(project(":core"))
    for (plugin in bukkitPlugins.plugins.flatMap { it.plugins.map { it.artifact } }) {
        compileOnly(plugin) {
            exclude("org.spigotmc", "spigot")
            exclude("org.bukkit", "bukkit")
            exclude("org.mcstats.bukkit", "metrics-lite")
            exclude("org.inventivetalent.packetlistener", "api")
        }
    }
}