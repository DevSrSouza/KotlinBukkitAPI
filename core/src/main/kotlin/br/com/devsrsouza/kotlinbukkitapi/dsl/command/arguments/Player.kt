package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit.onlinePlayers
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

// PLAYER

val PLAYER_MISSING_PARAMETER = "Missing player parameter!".color(ChatColor.RED)
val PLAYER_NOT_ONLINE = "The player specified is not online.".color(ChatColor.RED)

fun Executor<*>.playerOrNull(
        index: Int,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER
): Player? = string(index, argMissing).let { Bukkit.getPlayerExact(it) }

fun Executor<*>.player(
        index: Int,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
        notOnline: BaseComponent = PLAYER_NOT_ONLINE
): Player = playerOrNull(index, argMissing) ?: exception(notOnline)

inline fun <T : CommandSender> Executor<T>.argumentPlayer(
        notOnline: BaseComponent = PLAYER_NOT_ONLINE,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
        index: Int = 0,
        block: Executor<T>.(Player) -> Unit
) {
    val player = player(index, argMissing, notOnline)

    argumentExecutorBuilder(index + 1, player.name).block(player)
}

fun TabCompleter.player(
        index: Int
): List<String> = argumentCompleteBuilder(index) { arg ->
    onlinePlayers().mapNotNull {
        if(it.name.startsWith(arg, true)) it.name else null
    }
}

// OFFLINE PLAYER

fun Executor<*>.offlinePlayer(
        index: Int,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER
): OfflinePlayer = string(index, argMissing).let {
    runCatching { UUID.fromString(it) }.getOrNull()?.let { Bukkit.getOfflinePlayer(it) }
            ?: Bukkit.getOfflinePlayer(it)
}

inline fun <T : CommandSender> Executor<T>.argumentOfflinePlayer(
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
        index: Int = 0,
        block: Executor<T>.(OfflinePlayer) -> Unit
) {
    val player = offlinePlayer(index, argMissing)

    argumentExecutorBuilder(index + 1, player.name).block(player)
}

// GAMEMODE

val GAMEMODE_MISSING_PARAMETER = "Missing GameMode argument.".color(ChatColor.RED)
val GAMEMODE_NOT_FOUND = "The gamemode specified not found.".color(ChatColor.RED)

fun Executor<*>.gameModeOrNull(
        index: Int,
        argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER
): GameMode? = string(index, argMissing).run {
    toIntOrNull()?.let { GameMode.getByValue(it) } ?: runCatching { GameMode.valueOf(this.toUpperCase()) }.getOrNull()
}

fun Executor<*>.gameMode(
        index: Int,
        argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER,
        notFound: BaseComponent = GAMEMODE_NOT_FOUND
): GameMode = gameModeOrNull(index, argMissing) ?: exception(notFound)

inline fun <T : CommandSender> Executor<T>.argumentGameMode(
        argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER,
        notFound: BaseComponent = GAMEMODE_NOT_FOUND,
        index: Int = 0,
        block: Executor<T>.(GameMode) -> Unit
) {
    val gameMode = gameMode(index, argMissing, notFound)

    argumentExecutorBuilder(index + 1, gameMode.name).block(gameMode)
}

fun TabCompleter.gameMode(
        index: Int
): List<String> = argumentCompleteBuilder(index) { arg ->
    GameMode.values().mapNotNull {
        if(it.name.startsWith(arg, true)) it.name.toLowerCase() else null
    }
}