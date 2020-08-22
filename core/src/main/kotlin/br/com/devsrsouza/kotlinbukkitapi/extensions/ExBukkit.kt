package br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.msg
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

val server: Server get() = Bukkit.getServer()

object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {
    fun command(command: String) = Bukkit.dispatchCommand(this, command)
}

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

inline fun broadcast(
        players: Iterable<Player> = Bukkit.getOnlinePlayers(),
        message: Player.() -> String
) {
    for (player in players)
        player.msg(message.invoke(player))
}

fun Collection<Player>.broadcast(
        message: Player.() -> String
) = broadcast(this, message)

fun Array<Player>.broadcast(
        message: Player.() -> String
) = broadcast(this.toList(), message)

// BaseComponent

fun broadcast(message: BaseComponent) {
    broadcastComponent { arrayOf(message) }
    Bukkit.getConsoleSender().msg(TextComponent.toLegacyText(message))
}

fun broadcast(message: Array<BaseComponent>) {
    broadcastComponent { message }
    Bukkit.getConsoleSender().msg(TextComponent.toLegacyText(*message))
}

inline fun broadcastComponent(
        players: Iterable<Player> = Bukkit.getOnlinePlayers(),
        message: Player.() -> Array<BaseComponent>
) {
    for (player in players) {
        player.msg(message.invoke(player))
    }
}