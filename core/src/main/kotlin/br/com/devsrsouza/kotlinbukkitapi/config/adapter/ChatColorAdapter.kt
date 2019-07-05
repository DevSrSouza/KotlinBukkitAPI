package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.PropertyAdapter
import net.md_5.bungee.api.ChatColor
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChangeColor(val code: Char = '&')

fun chatColorSaveAdapter(): PropertyAdapter = adapter@ { _, property, any ->
    val color = property.findAnnotation<ChangeColor>()
    if (color != null) {
        (any as? String)?.run {
            return@adapter any.replace(ChatColor.COLOR_CHAR, color.code)
        } ?: return@adapter any
    } else return@adapter any
}

fun chatColorLoadAdapter(): PropertyAdapter = adapter@ { _, property, any ->
    val color = property.findAnnotation<ChangeColor>()
    if (color != null) {
        (any as? String)?.run {
            return@adapter ChatColor.translateAlternateColorCodes(color.code, any)
        } ?: return@adapter any
    } else return@adapter any
}
