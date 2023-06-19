package br.com.devsrsouza.kotlinbukkitapi.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.command.CommandFailException
import br.com.devsrsouza.kotlinbukkitapi.command.TabCompleter
import br.com.devsrsouza.kotlinbukkitapi.command.fail
import br.com.devsrsouza.kotlinbukkitapi.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import br.com.devsrsouza.kotlinbukkitapi.utility.extensions.FALSE_CASES
import br.com.devsrsouza.kotlinbukkitapi.utility.extensions.TRUE_CASES
import br.com.devsrsouza.kotlinbukkitapi.utility.extensions.toBooleanOrNull
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor

// STRING

public val MISSING_STRING_PARAMETER: TextComponent = "Missing a word argument.".color(ChatColor.RED)

public fun Executor<*>.string(
        index: Int,
        argMissing: BaseComponent = MISSING_STRING_PARAMETER
): String = args.getOrNull(index) ?: throw CommandFailException(argMissing, true)

public val TEXT_STRING_PARAMETER: TextComponent = "Missing a text argument.".color(ChatColor.RED)

public fun Executor<*>.text(
        startIndex: Int,
        endIndex: Int = args.size,
        separator: String = " ",
        argMissing: BaseComponent = TEXT_STRING_PARAMETER
): String {
    if(startIndex >= args.size) fail(argMissing)
    return array(startIndex, endIndex) { string(it) }.joinToString(separator)
}

// BOOLEAN

public val MISSING_BOOLEAN_PARAMETER: TextComponent = "Missing a true/false argument.".color(ChatColor.RED)
public val BOOLEAN_FORMAT: TextComponent = "The parameter needs only true or false.".color(ChatColor.RED)

/**
 * Returns [Boolean] or null if was not able to parse to Boolean.
 */
public fun Executor<*>.booleanOrNull(
    index: Int,
    argMissing: BaseComponent = MISSING_BOOLEAN_PARAMETER,
    trueCases: Array<String> = TRUE_CASES,
    falseCases: Array<String> = FALSE_CASES
): Boolean? = string(index, argMissing).toBooleanOrNull(trueCases, falseCases)

public fun Executor<*>.boolean(
    index: Int,
    argMissing: BaseComponent = MISSING_BOOLEAN_PARAMETER,
    booleanFormat: BaseComponent = BOOLEAN_FORMAT,
    trueCases: Array<String> = TRUE_CASES,
    falseCases: Array<String> = FALSE_CASES
): Boolean = booleanOrNull(index, argMissing, trueCases, falseCases) ?: fail(booleanFormat)

public fun TabCompleter.boolean(
        index: Int,
        trueCases: Array<String> = TRUE_CASES,
        falseCases: Array<String> = FALSE_CASES
): List<String> = argumentCompleteBuilder(index) { arg ->
    listOf(*trueCases, *falseCases).filter { it.startsWith(arg, true) }
}

public val MISSING_NUMBER_PARAMETER: TextComponent = "Missing a number argument.".color(ChatColor.RED)
public val NUMBER_FORMAT: TextComponent = "The parameter needs only numbers.".color(ChatColor.RED)

// INT

/**
 * Returns [Int] or null if was not able to parse to Int.
 */
public fun Executor<*>.intOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER
): Int? = string(index, argMissing).toIntOrNull()

public fun Executor<*>.int(
    index: Int,
    argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
    numberFormat: BaseComponent = NUMBER_FORMAT
): Int = intOrNull(index, argMissing) ?: fail(numberFormat)

// DOUBLE

/**
 * Returns [Double] or null if was not able to parse to Double.
 */
public fun Executor<*>.doubleOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER
): Double? = string(index, argMissing).toDoubleOrNull()?.takeIf { it.isFinite() }

public fun Executor<*>.double(
    index: Int,
    argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
    numberFormat: BaseComponent = NUMBER_FORMAT
): Double = doubleOrNull(index, argMissing) ?: fail(numberFormat)