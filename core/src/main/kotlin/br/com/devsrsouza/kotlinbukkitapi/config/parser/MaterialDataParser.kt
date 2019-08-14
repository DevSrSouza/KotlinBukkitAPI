package br.com.devsrsouza.kotlinbukkitapi.config.parser

import org.bukkit.Material
import org.bukkit.material.MaterialData
import java.lang.IllegalArgumentException
import kotlin.reflect.KType
import kotlin.reflect.full.createType

object MaterialDataParser : ObjectParser<MaterialData> {
    override fun parse(any: Any): MaterialData {
        if(any is String) {
            val slices = any.split(":")

            val type = slices[0].toIntOrNull()?.let { Material.getMaterial(it) }
                    ?: Material.values().first { it.name.equals(slices[0], true) }

            val data = slices.getOrNull(1)?.toIntOrNull()?.toByte() ?: 0

            return MaterialData(type, data)
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to MaterialData")
    }

    override fun render(element: MaterialData): Pair<Any, KType> {
        return "${element.itemType.name}:${element.data}" to String::class.createType()
    }
}