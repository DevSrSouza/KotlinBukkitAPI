package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Material
import org.bukkit.inventory.Inventory

val Inventory.hasSpace: Boolean
    get() = contents.any { it == null || it.type == Material.AIR }