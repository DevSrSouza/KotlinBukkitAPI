package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.projectiles.ProjectileSource
import org.bukkit.util.Vector
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public fun Entity.isPlayer(): Boolean {
    contract { returns(true) implies (this@isPlayer is Player) }

    return type == EntityType.PLAYER
}

//  firework

public inline fun firework(location: Location, block: FireworkMeta.() -> Unit): Firework {
    return location.spawn<Firework>().apply { meta(block) }
}

public inline fun Firework.meta(block: FireworkMeta.() -> Unit): Firework = apply {
    fireworkMeta = fireworkMeta.apply(block)
}

public inline fun <reified T : Projectile> ProjectileSource.launchProjectile(): T = launchProjectile(T::class.java)
public inline fun <reified T : Projectile> ProjectileSource.launchProjectile(vector: Vector): T = launchProjectile(T::class.java, vector)
