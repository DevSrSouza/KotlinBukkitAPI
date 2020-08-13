package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.collections.ExpirationList
import br.com.devsrsouza.kotlinbukkitapi.collections.ExpirationMap
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.*
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor

typealias ErrorHandler = Executor<*>.(Throwable) -> Unit

val defaultErrorHandler: ErrorHandler = {
    sender.msg(
            "An internal error occurred whilst executing this command".color(ChatColor.RED)
                    .showText(it.toString().color(ChatColor.RED))
    )

    it.printStackTrace()
}

class CommandFailException(
        val senderMessage: BaseComponent? = null,
        val argMissing: Boolean = false,
        inline val execute: suspend () -> Unit = {}
) : RuntimeException()

inline fun Executor<*>.fail(
        senderMessage: BaseComponent? = null,
        noinline execute: suspend () -> Unit = {}
): Nothing = throw CommandFailException(senderMessage, execute = execute)

inline fun Executor<*>.fail(
        senderMessage: String = "",
        noinline execute: suspend () -> Unit = {}
): Nothing = fail(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute = execute)

inline fun Executor<*>.fail(
        senderMessage: List<String> = listOf(),
        noinline execute: suspend () -> Unit = {}
): Nothing = fail(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute = execute)

// expiration collections

inline fun <T> ExpirationList<T>.failIfContains(
        element: T,
        execute: (missingTime: Int) -> Unit
) {
    missingTime(element)?.let(execute)?.run {
        throw CommandFailException()
    }
}

inline fun <K> ExpirationMap<K, *>.failIfContains(
        key: K,
        execute: (missingTime: Long) -> Unit
) {
    missingTime(key)?.let(execute)?.run {
        throw CommandFailException()
    }
}