object KotlinBukkitAPI {
    val version = "0.2.0-SNAPSHOT"

    val displayName = "KotlinBukkitAPI"
    val description = "KotlinBukkitAPI is an API for Bukkit/SpigotAPI using the cool and nifty features Kotlin has to make your life more easier."
    val github = "https://github.com/DevSrSouza/KotlinBukkitAPI"

    val plugins = listOf(
            Plugin("Vault", setOf("http://nexus.hc.to/content/repositories/pub_releases"), setOf("net.milkbowl.vault:VaultAPI:1.6")),
            Plugin("PlaceholderAPI", setOf("http://repo.extendedclip.com/content/repositories/placeholderapi/"), setOf("me.clip:placeholderapi:2.8.5")),
            Plugin("MVdWPlaceholderAPI", setOf("http://repo.mvdw-software.be/content/groups/public/"), setOf("be.maximvdw:MVdWPlaceholderAPI:2.5.2-SNAPSHOT")),
            Plugin("ActionBarAPI", setOf("https://jitpack.io"), setOf("com.github.ConnorLinfoot:ActionBarAPI:d60c2aedb9")),
            Plugin("TitleAPI", setOf("https://jitpack.io"), setOf("com.github.ConnorLinfoot:TitleAPI:1.7.6")),
            Plugin("WorldEdit", setOf("http://maven.sk89q.com/repo/"), setOf("com.sk89q.worldedit:worldedit-bukkit:6.1.5")),
            Plugin("ViaVersion", setOf("https://repo.viaversion.com/"), setOf("us.myles:viaversion-common:1.4.1")),
            Plugin("BossBarAPI", setOf("https://repo.inventivetalent.org/content/groups/public/"), setOf("org.inventivetalent:bossbarapi:2.4.1")),
            Plugin("HologramAPI", setOf("https://repo.inventivetalent.org/content/groups/public/"), setOf("org.inventivetalent:hologramapi:1.4.0")),
            Plugin("ProtocolLib", setOf("http://repo.dmulloy2.net/nexus/repository/public/"), setOf("com.comphenix.protocol:ProtocolLib:4.4.0"))
    )
}