package br.com.devsrsouza.kotlinbukkitapi.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.command.fail
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor

// intRange
public val MISSING_RANGE_PARAMETER: TextComponent = "Missing a range argument.".color(ChatColor.RED)
public val INT_RANGE_FORMAT: TextComponent = "The parameter needs a range of integer.".color(ChatColor.RED)

/**
 * Returns [IntRange] or null if was not able to parse to IntRange given the [separator].
 */
public fun Executor<*>.intRangeOrNull(
    index: Int,
    argMissing: BaseComponent = MISSING_RANGE_PARAMETER,
    separator: String = ".."
): IntRange? {
    val slices = string(index, argMissing).split(separator)
    val min = slices.getOrNull(0)?.toIntOrNull()
    val max = slices.getOrNull(1)?.toIntOrNull()

    return max?.let { min?.rangeTo(it) }
}

public fun Executor<*>.intRange(
    index: Int,
    argMissing: BaseComponent = MISSING_RANGE_PARAMETER,
    rangeFormat: BaseComponent = INT_RANGE_FORMAT,
    separator: String = ".."
): IntRange = intRangeOrNull(index, argMissing, separator)
        ?: fail(rangeFormat)
