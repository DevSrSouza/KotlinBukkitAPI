package br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.msg
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*

object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {
    fun command(command: String) = Bukkit.dispatchCommand(this, command)
}

fun mainWorld() = Bukkit.getWorlds()[0]
fun chunk(world: World, x: Int, y: Int) = world.getChunkAt(x, y)
fun chunk(block: Block) = chunk(block.world, block.x shr 4, block.z shr 4)
fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)
fun offlinePlayer(name: String) = Bukkit.getOfflinePlayer(name)
fun onlinePlayer(uuid: UUID) = Bukkit.getPlayer(uuid)
fun onlinePlayer(name: String) = Bukkit.getPlayerExact(name)
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

// BaseComponent

fun broadcast(message: BaseComponent) {
    broadcastComponent { arrayOf(message) }
    Bukkit.getConsoleSender().msg(TextComponent.toLegacyText(message))
}

fun broadcast(message: Array<BaseComponent>) {
    broadcastComponent { message }
    Bukkit.getConsoleSender().msg(TextComponent.toLegacyText(*message))
}

inline fun broadcastComponent(players: Iterable<Player> = Bukkit.getOnlinePlayers(), message: Player.() -> Array<BaseComponent>) {
    for (player in players) {
        player.msg(message.invoke(player))
    }
}