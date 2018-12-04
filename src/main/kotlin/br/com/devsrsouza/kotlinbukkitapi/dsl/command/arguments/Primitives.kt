package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.CommandException
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.argumentExecutorBuilder
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

val MISSING_NUMBER_PARAMETER = "Missing a number argument.".color(ChatColor.RED)
val NUMBER_FORMAT = "The parameter needs only numbers.".color(ChatColor.RED)

// INT

fun Executor<*>.int(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT
): Int = (args.getOrNull(index) ?: throw CommandException(argMissing))
        .toIntOrNull() ?: throw CommandException(numberFormat)

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

fun Executor<*>.double(
        index: Int,
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT
): Double = (args.getOrNull(index) ?: throw CommandException(argMissing))
        .toDoubleOrNull() ?: throw CommandException(numberFormat)

inline fun <T : CommandSender> Executor<T>.argumentDouble(
        argMissing: BaseComponent = MISSING_NUMBER_PARAMETER,
        numberFormat: BaseComponent = NUMBER_FORMAT,
        index: Int = 0,
        block: Executor<T>.(Double) -> Unit
) {
    val double = double(index, argMissing, numberFormat)

    argumentExecutorBuilder(index + 1, "$double").block(double)
}