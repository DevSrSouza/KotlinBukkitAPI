package br.com.devsrsouza.kotlinbukkitapi.dsl.command.arguments

import br.com.devsrsouza.kotlinbukkitapi.dsl.command.CommandException
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.Executor
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.argumentExecutorBuilder
import br.com.devsrsouza.kotlinbukkitapi.dsl.command.exception
import br.com.devsrsouza.kotlinbukkitapi.dsl.item.asMaterialData
import br.com.devsrsouza.kotlinbukkitapi.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.material.MaterialData

// MATERIAL

val MATERIAL_NOT_FOUND = "The item specified not found.".color(ChatColor.RED)
val MATERIAL_MISSING_PARAMETER = "Missing item argument.".color(ChatColor.RED)

private fun toMaterial(string: String) = string.toIntOrNull()?.let { Material.getMaterial(it) }
        ?: Material.getMaterial(string)

fun Executor<*>.materialOrNull(
        index: Int,
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER
): Material? = string(index, argMissing).run {
    toMaterial(this)
}

fun Executor<*>.material(
        index: Int,
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER,
        notFound: BaseComponent = MATERIAL_NOT_FOUND
): Material = materialOrNull(index, argMissing) ?: exception(notFound)

inline fun <T : CommandSender> Executor<T>.argumentMaterial(
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER,
        notFound: BaseComponent = MATERIAL_NOT_FOUND,
        index: Int = 0,
        block: Executor<T>.(Material) -> Unit
) {
    val material = material(index, argMissing, notFound)

    argumentExecutorBuilder(index + 1, material.name).block(material)
}

// MATERIAL DATA

val DATA_FORMAT = "The item data need be in number.".color(ChatColor.RED)

fun Executor<*>.materialDataOrNull(
        index: Int,
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER,
        dataFormat: BaseComponent = DATA_FORMAT
): MaterialData? = string(index, argMissing).run {
    val sliced = this.split(":")
    sliced.getOrNull(1)?.run {
        (toMaterial(sliced[0]))
                ?.asMaterialData(toIntOrNull()?.toByte() ?: exception(dataFormat))
    } ?: materialOrNull(index, argMissing)?.asMaterialData()
}

fun Executor<*>.materialData(
        index: Int,
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER,
        notFound: BaseComponent = MATERIAL_NOT_FOUND,
        dataFormat: BaseComponent = DATA_FORMAT
): MaterialData = materialDataOrNull(index, argMissing, dataFormat) ?: exception(notFound)

inline fun <T : CommandSender> Executor<T>.argumentMaterialData(
        argMissing: BaseComponent = MATERIAL_MISSING_PARAMETER,
        notFound: BaseComponent = MATERIAL_NOT_FOUND,
        dataFormat: BaseComponent = DATA_FORMAT,
        index: Int = 0,
        block: Executor<T>.(MaterialData) -> Unit
) {
    val material = materialData(index, argMissing, notFound, dataFormat)

    argumentExecutorBuilder(index + 1, "${material.itemType.name}:${material.data}").block(material)
}