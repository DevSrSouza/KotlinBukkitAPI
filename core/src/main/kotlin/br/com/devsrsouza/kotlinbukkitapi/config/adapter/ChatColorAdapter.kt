package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.PropertyLoadAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.PropertySaveAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.isFirstGenericString
import net.md_5.bungee.api.ChatColor
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChangeColor(val code: Char = '&')

fun chatColorSaveAdapter(): PropertySaveAdapter = adapter@ { _, property, type, any ->
    val color = property.findAnnotation<ChangeColor>()
    if (color != null) {
        fun translate(str: String) = str.replace(ChatColor.COLOR_CHAR, color.code)
        if(any is String) {
            return@adapter translate(any) to type
        } else if(any is List<*> && type.isFirstGenericString) {
            return@adapter (any.map { translate(it as String) }) to type
        } else return@adapter any to type
    } else return@adapter any to type
}

fun chatColorLoadAdapter(): PropertyLoadAdapter = adapter@ { _, property, any ->
    val type = property.returnType
    val color = property.findAnnotation<ChangeColor>()
    if (color != null) {
        fun translate(str: String) = ChatColor.translateAlternateColorCodes(color.code, str)
        if(any is String) {
            return@adapter translate(any)
        } else if(any is List<*> && type.isFirstGenericString) {
            return@adapter any.map { translate(it as String) }
        } else return@adapter any
    } else return@adapter any
}
