package br.com.devsrsouza.kotlinbukkitapi.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.command.TabCompleter
import br.com.devsrsouza.kotlinbukkitapi.command.fail
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import br.com.devsrsouza.kotlinbukkitapi.extensions.onlinePlayers
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.Locale
import java.util.UUID

// PLAYER

public val PLAYER_MISSING_PARAMETER: TextComponent = "Missing player parameter!".color(ChatColor.RED)
public val PLAYER_NOT_ONLINE: TextComponent = "The player specified is not online.".color(ChatColor.RED)

/**
 * returns a [Player] or null if the player is not online.
 */
public fun Executor<*>.playerOrNull(
    index: Int,
    argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
): Player? = string(index, argMissing).let { Bukkit.getPlayerExact(it) }

public fun Executor<*>.player(
    index: Int,
    argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
    notOnline: BaseComponent = PLAYER_NOT_ONLINE,
): Player = playerOrNull(index, argMissing) ?: fail(notOnline)

public fun TabCompleter.player(
    index: Int,
): List<String> = argumentCompleteBuilder(index) { arg ->
    onlinePlayers.mapNotNull {
        if (it.name.startsWith(arg, true)) it.name else null
    }
}

// OFFLINE PLAYER

public fun Executor<*>.offlinePlayer(
    index: Int,
    argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
): OfflinePlayer = string(index, argMissing).let {
    runCatching { UUID.fromString(it) }.getOrNull()?.let { Bukkit.getOfflinePlayer(it) }
        ?: Bukkit.getOfflinePlayer(it)
}

// GAMEMODE

public val GAMEMODE_MISSING_PARAMETER: TextComponent = "Missing GameMode argument.".color(ChatColor.RED)
public val GAMEMODE_NOT_FOUND: TextComponent = "The gamemode specified not found.".color(ChatColor.RED)

/**
 * returns a [GameMode] or null if was not found.
 */
public fun Executor<*>.gameModeOrNull(
    index: Int,
    argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER,
): GameMode? = string(index, argMissing).run {
    toIntOrNull()?.let { GameMode.getByValue(it) } ?: runCatching { GameMode.valueOf(this.uppercase(Locale.getDefault())) }.getOrNull()
}

public fun Executor<*>.gameMode(
    index: Int,
    argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER,
    notFound: BaseComponent = GAMEMODE_NOT_FOUND,
): GameMode = gameModeOrNull(index, argMissing) ?: fail(notFound)

public fun TabCompleter.gameMode(
    index: Int,
): List<String> = argumentCompleteBuilder(index) { arg ->
    GameMode.values().mapNotNull {
        if (it.name.startsWith(arg, true)) it.name.lowercase(Locale.getDefault()) else null
    }
}
