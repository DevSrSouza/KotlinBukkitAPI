package br.com.devsrsouza.kotlinbukkitapi.extensions

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

public val server: Server get() = Bukkit.getServer()

public object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {
    public fun command(command: String): Boolean = Bukkit.dispatchCommand(this, command)
}

// logger
// todo: remove and use standard logging library, maybe extensions for it
public object Log {
    public fun info(message: String): Unit = Bukkit.getLogger().info(message)
    public fun warn(message: String): Unit = Bukkit.getLogger().warning(message)
    public fun severe(message: String): Unit = Bukkit.getLogger().severe(message)
    public fun fine(message: String): Unit = Bukkit.getLogger().fine(message)
}

public fun broadcast(message: String) {
    Bukkit.broadcastMessage(message)
}

public inline fun broadcast(
    players: Iterable<Player> = Bukkit.getOnlinePlayers(),
    message: Player.() -> String,
) {
    for (player in players)
        player.msg(message.invoke(player))
}

public fun Collection<Player>.broadcast(
    message: Player.() -> String,
): Unit = broadcast(this, message)

public fun Array<Player>.broadcast(
    message: Player.() -> String,
): Unit = broadcast(this.toList(), message)

// BaseComponent

public fun broadcast(message: BaseComponent) {
    broadcastComponent { arrayOf(message) }
    Bukkit.getConsoleSender().msg(TextComponent.toLegacyText(message))
}

public fun broadcast(message: Array<BaseComponent>) {
    broadcastComponent { message }
    Bukkit.getConsoleSender().msg(TextComponent.toLegacyText(*message))
}

public inline fun broadcastComponent(
    players: Iterable<Player> = Bukkit.getOnlinePlayers(),
    message: Player.() -> Array<BaseComponent>,
) {
    for (player in players) {
        player.msg(message.invoke(player))
    }
}
