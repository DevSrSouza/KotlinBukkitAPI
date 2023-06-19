package br.com.devsrsouza.kotlinbukkitapi.command

import br.com.devsrsouza.kotlinbukkitapi.extensions.asText
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import br.com.devsrsouza.kotlinbukkitapi.extensions.msg
import br.com.devsrsouza.kotlinbukkitapi.extensions.showText
import br.com.devsrsouza.kotlinbukkitapi.utility.collections.ExpirationList
import br.com.devsrsouza.kotlinbukkitapi.utility.collections.ExpirationMap
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor

public typealias ErrorHandler = Executor<*>.(Throwable) -> Unit

public val defaultErrorHandler: ErrorHandler = {
    sender.msg(
        "An internal error occurred whilst executing this command".color(ChatColor.RED)
            .showText(it.toString().color(ChatColor.RED)),
    )

    it.printStackTrace()
}

public class CommandFailException(
    public val senderMessage: BaseComponent? = null,
    public val argMissing: Boolean = false,
    public inline val execute: suspend () -> Unit = {},
) : RuntimeException()

public inline fun Executor<*>.fail(
    senderMessage: BaseComponent? = null,
    noinline execute: suspend () -> Unit = {},
): Nothing = throw CommandFailException(senderMessage, execute = execute)

public inline fun Executor<*>.fail(
    senderMessage: String = "",
    noinline execute: suspend () -> Unit = {},
): Nothing = fail(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute = execute)

public inline fun Executor<*>.fail(
    senderMessage: List<String> = listOf(),
    noinline execute: suspend () -> Unit = {},
): Nothing = fail(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute = execute)

// expiration collections

public inline fun <T> ExpirationList<T>.failIfContains(
    element: T,
    execute: (missingTime: Int) -> Unit,
) {
    missingTime(element)?.let(execute)?.run {
        throw CommandFailException()
    }
}

public inline fun <K> ExpirationMap<K, *>.failIfContains(
    key: K,
    execute: (missingTime: Long) -> Unit,
) {
    missingTime(key)?.let(execute)?.run {
        throw CommandFailException()
    }
}
