package br.com.devsrsouza.kotlinbukkitapi.extensions.entity

import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.FireworkMeta
import kotlin.contracts.contract

val Entity.isPlayer: Boolean
    get() {
        contract { returns(true) implies (this@isPlayer is Player) }
        return type == EntityType.PLAYER
    }

inline fun Firework.meta(block: FireworkMeta.() -> Unit) = apply {
    fireworkMeta = fireworkMeta.apply(block)
}