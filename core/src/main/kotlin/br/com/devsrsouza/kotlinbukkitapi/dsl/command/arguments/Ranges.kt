package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.exception
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor

// intRange
val MISSING_RANGE_PARAMETER = "Missing a range argument.".color(ChatColor.RED)
val INT_RANGE_FORMAT = "The parameter needs a range of integer.".color(ChatColor.RED)

fun Executor<*>.intRangeOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_RANGE_PARAMETER,
        separator: String = ".."
): IntRange? {
    val slices = string(index, argMissing).split(separator)
    val min = slices.getOrNull(0)?.toIntOrNull()
    val max = slices.getOrNull(1)?.toIntOrNull()

    return max?.let { min?.rangeTo(it) }
}

fun Executor<*>.intRange(
        index: Int,
        argMissing: BaseComponent = MISSING_RANGE_PARAMETER,
        rangeFormat: BaseComponent = INT_RANGE_FORMAT,
        separator: String = ".."
): IntRange = intRangeOrNull(index, argMissing, separator)
        ?: exception(rangeFormat)
