package br.com.devsrsouza.kotlinbukkitapi.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.command.fail
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import br.com.devsrsouza.kotlinbukkitapi.utility.extensions.getIgnoreCase
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor

// enum
public val MISSING_ENUM_PARAMETER: TextComponent = "Missing an enum argument.".color(ChatColor.RED)
public val ENUM_VALUE_NOT_FOUND: TextComponent = "The value name specified not found.".color(ChatColor.RED)

/**
 * Returns [T] or null if was not able to find in the [Enum].
 */
public inline fun <reified T : Enum<T>> Executor<*>.enumOrNull(
    index: Int,
    argMissing: BaseComponent = MISSING_ENUM_PARAMETER,
    additionalNames: Map<String, T> = mapOf(),
): T? {
    val name = string(index, argMissing)
    return enumValues<T>().find { it.name.equals(name, true) }
        ?: additionalNames.getIgnoreCase(name)
}

public inline fun <reified T : Enum<T>> Executor<*>.enum(
    index: Int,
    argMissing: BaseComponent = MISSING_ENUM_PARAMETER,
    notFound: BaseComponent = ENUM_VALUE_NOT_FOUND,
    additionalNames: Map<String, T> = mapOf(),
): T = enumOrNull(index, argMissing, additionalNames) ?: fail(notFound)
