package br.com.devsrsouza.kotlinbukkitapi.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.command.TabCompleter
import br.com.devsrsouza.kotlinbukkitapi.command.fail
import br.com.devsrsouza.kotlinbukkitapi.command.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.color
import java.util.Locale
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import net.md_5.bungee.api.chat.TextComponent

// MATERIAL

public val MATERIAL_NOT_FOUND: TextComponent = "The item specified not found.".color(ChatColor.RED)
public val MATERIAL_MISSING_PARAMETER: TextComponent = "Missing item argument.".color(ChatColor.RED)

private fun toMaterial(string: String) = Material.getMaterial(string.uppercase(Locale.getDefault()))

/**
 * Returns [Material] or null if the Material was not found.
 */
public fun Executor<*>.materialOrNull(
        index: Int,
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER
): Material? = string(index, argMissing).run {
    toMaterial(this)
}

public fun Executor<*>.material(
    index: Int,
    argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER,
    notFound: BaseComponent = MATERIAL_NOT_FOUND
): Material = materialOrNull(index, argMissing) ?: fail(notFound)

public fun TabCompleter.material(
        index: Int
): List<String> = argumentCompleteBuilder(index) { arg ->
    Material.values().mapNotNull {
        if(it.name.startsWith(arg, true)) it.name.lowercase(Locale.getDefault()) else null
    }
}
