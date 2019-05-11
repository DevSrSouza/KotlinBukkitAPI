package br.com.devsrsouza.kotlinbukkitapi.extensions.inventory

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

val Inventory.hasSpace: Boolean
    get() = contents.any { it == null || it.type == Material.AIR }

fun Inventory.hasSpace(
        item: ItemStack,
        amount: Int = item.amount
) = spaceOf(item) >= amount

fun Inventory.spaceOf(
        item: ItemStack
): Int {
    return contents.filterNotNull().map {
        if (it.amount < it.maxStackSize && it.isSimilar(item))
            it.maxStackSize - it.amount
        else 0
    }.count()
}