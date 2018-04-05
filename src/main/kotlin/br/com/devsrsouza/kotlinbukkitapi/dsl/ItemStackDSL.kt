package br.com.devsrsouza.kotlinbukkitapi.dsl.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

inline fun <T : ItemMeta> ItemStack.meta(block: T.() -> Unit) {
    itemMeta = (itemMeta as T).apply(block)
}