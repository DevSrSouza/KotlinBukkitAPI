package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor.*
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

// WORLD

val MISSING_WORLD_ARGUMENT = "Missing a world argument.".color(RED)
val WORLD_NOT_FOUND = "World typed not found.".color(RED)

fun Executor<*>.worldOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_WORLD_ARGUMENT
): World? = string(index, argMissing).let { Bukkit.getWorld(it) }

fun Executor<*>.world(
        index: Int,
        argMissing: BaseComponent = MISSING_WORLD_ARGUMENT,
        notFound: BaseComponent = WORLD_NOT_FOUND
): World = worldOrNull(index, argMissing) ?: exception(notFound)

inline fun <T : CommandSender> Executor<T>.argumentWorld(
        argMissing: BaseComponent = MISSING_WORLD_ARGUMENT,
        notFound: BaseComponent = WORLD_NOT_FOUND,
        index: Int = 0,
        block: Executor<T>.(World) -> Unit
) {
    val world = world(index, argMissing, notFound)

    argumentExecutorBuilder(
            index + 1,
            world.name
    ).block(world)
}

fun TabCompleter.world(
        index: Int
): List<String> = argumentCompleteBuilder(index) { arg ->
    Bukkit.getWorlds().mapNotNull {
        if(it.name.startsWith(arg, true)) it.name else null
    }
}

// COORDINATE

val MISSING_COORDINATE_ARGUMENT = "Missing coordinate argument. Argument format [x] [y] [z]".color(RED)
val COORDINATE_NUMBER_FORMAT = "Please, type numbers for coordinates".color(RED)

fun Executor<Player>.coordinate(
        xIndex: Int, yIndex: Int, zIndex: Int,
        argMissing: BaseComponent = MISSING_COORDINATE_ARGUMENT,
        numberFormat: BaseComponent = COORDINATE_NUMBER_FORMAT
): Location = coordinate(xIndex, yIndex, zIndex, sender.world, argMissing, numberFormat)

fun Executor<*>.coordinate(
        xIndex: Int, yIndex: Int, zIndex: Int, world: World,
        argMissing: BaseComponent = MISSING_COORDINATE_ARGUMENT,
        numberFormat: BaseComponent = COORDINATE_NUMBER_FORMAT
): Location {

    fun double(index: Int) = double(index, argMissing, numberFormat)

    return Location(world, double(xIndex), double(yIndex), double(zIndex))
}

inline fun <T : CommandSender> Executor<T>.argumentCoordinate(
        world: World,
        argMissing: BaseComponent = MISSING_COORDINATE_ARGUMENT,
        numberFormat: BaseComponent = COORDINATE_NUMBER_FORMAT,
        startIndex: Int = 0,
        block: Executor<T>.(Location) -> Unit
) {
    val location = coordinate(startIndex,
            startIndex + 1,
            startIndex + 2,
            world, argMissing, numberFormat)

    argumentExecutorBuilder(
            startIndex + 3,
            "${location.x} ${location.y} ${location.z}"
    ).block(location)
}