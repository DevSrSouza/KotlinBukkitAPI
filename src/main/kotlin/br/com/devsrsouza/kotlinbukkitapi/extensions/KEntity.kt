package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.entity.Firework
import org.bukkit.inventory.meta.FireworkMeta

inline fun Firework.meta(block: FireworkMeta.() -> Unit) = apply {
    fireworkMeta = fireworkMeta.apply(block)
}