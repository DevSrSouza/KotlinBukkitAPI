package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.PropertyAdapter
import br.com.devsrsouza.kotlinbukkitapi.config.isListString
import net.md_5.bungee.api.ChatColor
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChangeColor(val code: Char = '&')

fun chatColorSaveAdapter(): PropertyAdapter = adapter@ { _, property, any ->
    val color = property.findAnnotation<ChangeColor>()
    if (color != null) {
        fun translate(str: String) = str.replace(ChatColor.COLOR_CHAR, color.code)
        if(any is String) {
            return@adapter translate(any)
        } else if(any is List<*> && isListString(property)) {
            return@adapter any.map { translate(it as String) }
        } else return@adapter any
    } else return@adapter any
}

fun chatColorLoadAdapter(): PropertyAdapter = adapter@ { _, property, any ->
    val color = property.findAnnotation<ChangeColor>()
    if (color != null) {
        fun translate(str: String) = ChatColor.translateAlternateColorCodes(color.code, str)
        if(any is String) {
            return@adapter translate(any)
        } else if(any is List<*> && isListString(property)) {
            return@adapter any.map { translate(it as String) }
        } else return@adapter any
    } else return@adapter any
}
