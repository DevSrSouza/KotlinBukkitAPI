package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.CommandException
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.argumentExecutorBuilder
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

// STRING

val MISSING_STRING_PARAMETER = "Missing a word argument.".color(ChatColor.RED)

fun Executor<*>.string(
        index: Int,
        argMissing: BaseComponent = MISSING_STRING_PARAMETER
): String = args.getOrNull(index) ?: throw CommandException(argMissing, argMissing = true)

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
): Int = intOrNull(index, argMissing) ?: throw CommandException(numberFormat)

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
): Double = doubleOrNull(index, argMissing) ?: throw CommandException(numberFormat)

inline fun <T : CommandSender> Executor<T>.argumentDouble(
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT,
        index: Int = 0,
        block: Executor<T>.(Double) -> Unit
) {
    val double = double(index, argMissing, numberFormat)

    argumentExecutorBuilder(index + 1, "$double").block(double)
}