package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Console {
    val sender get() = Bukkit.getConsoleSender()

    fun command(command: String) = Bukkit.dispatchCommand(sender, command)
    fun message(message: String) = sender.sendMessage(message)
    fun sendMessage(message: String) = message(message)
}

fun broadcast(message: String) {
    Bukkit.broadcastMessage(message)
}

inline fun broadcast(senders: Iterable<Player> = Bukkit.getOnlinePlayers(), message: Player.() -> String) {
    for (sender in senders) {
        sender.sendMessage(message.invoke(sender))
    }
}

fun Collection<Player>.broadcast(message: Player.() -> String) = broadcast(this, message)
fun Array<Player>.broadcast(message: Player.() -> String) = broadcast(this.toList(), message)
