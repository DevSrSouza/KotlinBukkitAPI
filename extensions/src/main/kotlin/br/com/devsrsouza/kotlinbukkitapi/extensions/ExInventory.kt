package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

public val Inventory.hasSpace: Boolean
    get() = contents.any { it == null || it.type == Material.AIR }

public fun Inventory.hasSpace(
    item: ItemStack,
    amount: Int = item.amount,
): Boolean = spaceOf(item) >= amount

public fun Inventory.spaceOf(
    item: ItemStack,
): Int {
    return contents.filterNotNull().map {
        if (it.amount < it.maxStackSize && it.isSimilar(item)) {
            it.maxStackSize - it.amount
        } else {
            0
        }
    }.count()
}
