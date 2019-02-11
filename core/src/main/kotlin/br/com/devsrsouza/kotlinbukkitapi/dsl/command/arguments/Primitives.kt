package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import br.com.devsrsouza.kotlinbukkitapi.utils.FALSE_CASES
import br.com.devsrsouza.kotlinbukkitapi.utils.TRUE_CASES
import br.com.devsrsouza.kotlinbukkitapi.utils.toBooleanOrNull
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

// STRING

val MISSING_STRING_PARAMETER = "Missing a word argument.".color(ChatColor.RED)

fun Executor<*>.string(
        index: Int,
        argMissing: BaseComponent = MISSING_STRING_PARAMETER
): String = args.getOrNull(index) ?: throw CommandException(argMissing, true)

// BOOLEAN

val MISSING_BOOLEAN_PARAMETER = "Missing a true/false argument.".color(ChatColor.RED)
val BOOLEAN_FORMAT = "The parameter needs only true or false.".color(ChatColor.RED)

fun Executor<*>.booleanOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_BOOLEAN_PARAMETER,
        trueCases: Array<String> = TRUE_CASES,
        falseCases: Array<String> = FALSE_CASES
): Boolean? = string(index, argMissing).toBooleanOrNull(trueCases, falseCases)

fun Executor<*>.boolean(
        index: Int,
        argMissing: BaseComponent = MISSING_BOOLEAN_PARAMETER,
        booleanFormat: BaseComponent = BOOLEAN_FORMAT,
        trueCases: Array<String> = TRUE_CASES,
        falseCases: Array<String> = FALSE_CASES
): Boolean = booleanOrNull(index, argMissing, trueCases, falseCases) ?: exception(booleanFormat)

inline fun <T : CommandSender> Executor<T>.booleanArgument(
        argMissing: BaseComponent = MISSING_BOOLEAN_PARAMETER,
        booleanFormat: BaseComponent = BOOLEAN_FORMAT,
        trueCases: Array<String> = TRUE_CASES,
        falseCases: Array<String> = FALSE_CASES,
        index: Int = 0,
        block: Executor<T>.(Boolean) -> Unit
) {
    val boolean = boolean(index, argMissing, booleanFormat, trueCases, falseCases)

    argumentExecutorBuilder(index + 1, "$boolean").block(boolean)
}

fun TabCompleter.boolean(
        index: Int,
        trueCases: Array<String> = TRUE_CASES,
        falseCases: Array<String> = FALSE_CASES
): List<String> = argumentCompleteBuilder(index) { arg ->
    listOf(*trueCases, *falseCases).filter { it.startsWith(arg, true) }
}

val MISSING_NUMBER_PARAMETER = "Missing a number argument.".color(ChatColor.RED)
val NUMBER_FORMAT = "The parameter needs only numbers.".color(ChatColor.RED)

// INT

fun Executor<*>.intOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER
): Int? = string(index, argMissing).toIntOrNull()

fun Executor<*>.int(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT
): Int = intOrNull(index, argMissing) ?: exception(numberFormat)

inline fun <T : CommandSender> Executor<T>.argumentInt(
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT,
        index: Int = 0,
        block: Executor<T>.(Int) -> Unit
) {
    val int = int(index, argMissing, numberFormat)

    argumentExecutorBuilder(index + 1, "$int").block(int)
}

// DOUBLE

fun Executor<*>.doubleOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER
): Double? = string(index, argMissing).toDoubleOrNull()?.takeUnless { it.isNaN() }

fun Executor<*>.double(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT
): Double = doubleOrNull(index, argMissing) ?: exception(numberFormat)

inline fun <T : CommandSender> Executor<T>.argumentDouble(
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT,
        index: Int = 0,
        block: Executor<T>.(Double) -> Unit
) {
    val double = double(index, argMissing, numberFormat)

    argumentExecutorBuilder(index + 1, "$double").block(double)
}