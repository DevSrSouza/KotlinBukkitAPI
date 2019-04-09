package br.com.devsrsouza.kotlinbukkitapi.utils.server

import org.bukkit.Server

sealed class ServerImplementation {
    abstract class BukkitImplementation(val nms: String) : ServerImplementation()

    class CraftBukkit(nms: String) : BukkitImplementation(nms)
    class Spigot(nms: String) : BukkitImplementation(nms)
    class PaperSpigot(nms: String) : BukkitImplementation(nms)
    class TacoSpigot(nms: String) : BukkitImplementation(nms)
    class UnknownBukkitFork(nms: String) : BukkitImplementation(nms)

    object Glowstone : ServerImplementation()
    class UnknownImplementation(val name: String) : ServerImplementation()
}

val Server.implementation: ServerImplementation get() {
    return when(name) {
        "GlowStone" -> ServerImplementation.Glowstone
        "CraftBukkit" ->  {
            val pack = this::class.java.`package`

            val impl = pack.implementationVersion.split("-").getOrNull(1)
            val nms = pack.name.substringAfterLast(".", "")

            when(impl) {
                "CraftBukkit" -> ServerImplementation.CraftBukkit(nms)
                "Spigot" -> ServerImplementation.Spigot(nms)
                "PaperSpigot" -> ServerImplementation.PaperSpigot(nms)
                "TacoSpigot" -> ServerImplementation.TacoSpigot(nms)
                else -> ServerImplementation.UnknownBukkitFork(nms)
            }
        }
        else -> ServerImplementation.UnknownImplementation(name)
    }
}

val Server.apiVersion get() = ApiVersion(this)

inline class ApiVersion(val server: Server) {
    private val version get() = server.bukkitVersion.split("-")[0].split(".")

    val major: Int get() = version[0].toInt()
    val minor: Int get() = version[1].toInt()
    val patch: Int get() = version[2].toIntOrNull() ?: 0
}