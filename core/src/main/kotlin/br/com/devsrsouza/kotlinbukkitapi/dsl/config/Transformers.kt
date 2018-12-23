package br.com.devsrsouza.kotlinbukkitapi.dsl.config

import org.bukkit.ChatColor
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChangeColor(val code: Char = '&')

fun KProperty1<*, *>.loadTransformerChangeColor(obj: Any): Any {
    val color = findAnnotation<ChangeColor>()
    if (color != null) {
        (obj as? String)?.run {
            return ChatColor.translateAlternateColorCodes(color.code, obj)
        } ?: return obj
    } else return obj
}

fun KProperty1<*, *>.saveTransformerChangeColor(obj: Any): Any {
    val color = findAnnotation<ChangeColor>()
    if (color != null) {
        (obj as? String)?.run {
            return obj.replace(ChatColor.COLOR_CHAR, color.code)
        } ?: return obj
    } else return obj
}