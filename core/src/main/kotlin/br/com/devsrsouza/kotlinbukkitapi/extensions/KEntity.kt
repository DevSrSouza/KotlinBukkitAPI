package br.com.devsrsouza.kotlinbukkitapi.extensions.entity

import br.com.devsrsouza.kotlinbukkitapi.extensions.location.spawn
import org.bukkit.Location
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

//  firework

inline fun firework(location: Location, block: FireworkMeta.() -> Unit): Firework {
    return location.spawn<Firework>().apply { meta(block) }
}

inline fun Firework.meta(block: FireworkMeta.() -> Unit) = apply {
    fireworkMeta = fireworkMeta.apply(block)
}