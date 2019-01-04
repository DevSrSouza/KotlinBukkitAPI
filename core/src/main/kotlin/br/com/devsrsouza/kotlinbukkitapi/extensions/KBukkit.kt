package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {
    fun command(command: String) = Bukkit.dispatchCommand(this, command)
}

fun mainWorld() = Bukkit.getWorlds()[0]

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
