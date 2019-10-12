package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.fail
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import br.com.devsrsouza.kotlinbukkitapi.utils.getIgnoreCase
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor

// enum
val MISSING_ENUM_PARAMETER = "Missing an enum argument.".color(ChatColor.RED)
val ENUM_VALUE_NOT_FOUND = "The value name specified not found.".color(ChatColor.RED)

/**
 * Returns [T] or null if was not able to find in the [Enum].
 */
inline fun <reified T : Enum<T>> Executor<*>.enumOrNull(
        index: Int,
        argMissing: BaseComponent = MISSING_ENUM_PARAMETER,
        additionalNames: Map<String, T> = mapOf()
): T? {
    val name = string(index, argMissing)
    return enumValues<T>().find { it.name.equals(name, true) }
            ?: additionalNames.getIgnoreCase(name)
}

inline fun <reified T : Enum<T>> Executor<*>.enum(
        index: Int,
        argMissing: BaseComponent = MISSING_ENUM_PARAMETER,
        notFound: BaseComponent = ENUM_VALUE_NOT_FOUND,
        additionalNames: Map<String, T> = mapOf()
): T = enumOrNull(index, argMissing, additionalNames) ?: fail(notFound)