package br.com.devsrsouza.kotlinbukkitapi.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.command.TabCompleter
import br.com.devsrsouza.kotlinbukkitapi.command.fail
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import net.md_5.bungee.api.ChatColor.RED
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

// WORLD

public val MISSING_WORLD_ARGUMENT: TextComponent = "Missing a world argument.".color(RED)
public val WORLD_NOT_FOUND: TextComponent = "World typed not found.".color(RED)

/**
 * Returns [World] or null if was not found.
 */
public fun Executor<*>.worldOrNull(
    index: Int,
    argMissing: BaseComponent = MISSING_WORLD_ARGUMENT,
): World? = string(index, argMissing).let { Bukkit.getWorld(it) }

public fun Executor<*>.world(
    index: Int,
    argMissing: BaseComponent = MISSING_WORLD_ARGUMENT,
    notFound: BaseComponent = WORLD_NOT_FOUND,
): World = worldOrNull(index, argMissing) ?: fail(notFound)

public fun TabCompleter.world(
    index: Int,
): List<String> = argumentCompleteBuilder(index) { arg ->
    Bukkit.getWorlds().mapNotNull {
        if (it.name.startsWith(arg, true)) it.name else null
    }
}

// COORDINATE

public val MISSING_COORDINATE_ARGUMENT: TextComponent = "Missing coordinate argument. Argument format [x] [y] [z]".color(RED)
public val COORDINATE_NUMBER_FORMAT: TextComponent = "Please, type numbers for coordinates".color(RED)

public fun Executor<Player>.coordinate(
    xIndex: Int,
    yIndex: Int,
    zIndex: Int,
    argMissing: BaseComponent = MISSING_COORDINATE_ARGUMENT,
    numberFormat: BaseComponent = COORDINATE_NUMBER_FORMAT,
): Location = coordinate(xIndex, yIndex, zIndex, sender.world, argMissing, numberFormat)

public fun Executor<*>.coordinate(
    xIndex: Int,
    yIndex: Int,
    zIndex: Int,
    world: World,
    argMissing: BaseComponent = MISSING_COORDINATE_ARGUMENT,
    numberFormat: BaseComponent = COORDINATE_NUMBER_FORMAT,
): Location {
    fun double(index: Int) = double(index, argMissing, numberFormat)

    return Location(world, double(xIndex), double(yIndex), double(zIndex))
}
