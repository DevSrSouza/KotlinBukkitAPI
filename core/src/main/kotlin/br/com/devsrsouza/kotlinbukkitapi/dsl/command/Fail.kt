package br.com.devsrsouza.kotlinbukkitapi.dsl.command

import br.com.devsrsouza.kotlinbukkitapi.extensions.text.asText
import net.md_5.bungee.api.chat.BaseComponent

class CommandFailException(
        val senderMessage: BaseComponent? = null,
        val argMissing: Boolean = false,
        val execute: () -> Unit = {}
) : RuntimeException()

inline fun Executor<*>.fail(
        senderMessage: BaseComponent? = null,
        noinline execute: () -> Unit = {}
): Nothing = throw CommandFailException(senderMessage, execute = execute)

inline fun Executor<*>.fail(
        senderMessage: String = "",
        noinline execute: () -> Unit = {}
): Nothing = throw CommandFailException(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute = execute)

inline fun Executor<*>.fail(
        senderMessage: List<String> = listOf(),
        noinline execute: () -> Unit = {}
): Nothing = throw CommandFailException(senderMessage.takeIf { it.isNotEmpty() }?.asText(), execute = execute)