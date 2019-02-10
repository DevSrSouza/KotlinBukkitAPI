package br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit

import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*

object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {
    fun command(command: String) = Bukkit.dispatchCommand(this, command)
}

fun mainWorld() = Bukkit.getWorlds()[0]
fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)
fun offlinePlayer(name: String) = Bukkit.getOfflinePlayer(name)
fun onlinePlayers() = Bukkit.getOnlinePlayers()

// logger
object Log {
    fun info(message: String) = Bukkit.getLogger().info(message)
    fun warn(message: String) = Bukkit.getLogger().warning(message)
    fun severe(message: String) = Bukkit.getLogger().severe(message)
    fun fine(message: String) = Bukkit.getLogger().fine(message)
}

fun broadcast(message: String) {
    Bukkit.broadcastMessage(message)
}

inline fun broadcast(players: Iterable<Player> = Bukkit.getOnlinePlayers(), message: Player.() -> String) {
    for (player in players) {
        player.sendMessage(message.invoke(player))
    }
}

fun Collection<Player>.broadcast(message: Player.() -> String) = broadcast(this, message)
fun Array<Player>.broadcast(message: Player.() -> String) = broadcast(this.toList(), message)
