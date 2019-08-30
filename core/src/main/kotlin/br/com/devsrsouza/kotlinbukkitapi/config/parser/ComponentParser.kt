package br.com.devsrsouza.kotlinbukkitapi.config.parser

import com.google.gson.Gson
import net.md_5.bungee.api.chat.*
import net.md_5.bungee.chat.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

object ComponentParser : ObjectParser<TextComponent> {

    private val gson = Gson()

    override fun parse(any: Any): TextComponent {
        if(any is Map<*,*>) {
            val json = gson.toJson(any)
            return TextComponent(*ComponentSerializer.parse(json))
        } else if(any is List<*>) {
            val json = gson.toJson(any)
            return TextComponent(*ComponentSerializer.parse(json))
        } else throw IllegalArgumentException("can't parse ${any::class.simpleName} to TextComponent")
    }

    override fun render(element: TextComponent): Pair<Any, KType> {
        val json = ComponentSerializer.toString(element)
        return gson.fromJson<Map<*, *>>(json, Map::class.java) to Map::class.createType(
            listOf(
                KTypeProjection.invariant(String::class.createType()),
                KTypeProjection.invariant(Any::class.createType())
            )
        )
    }

}
