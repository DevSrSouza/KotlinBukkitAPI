package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.LightningStrike
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import org.bukkit.util.Vector

public operator fun Location.component1(): Double = x
public operator fun Location.component2(): Double = y
public operator fun Location.component3(): Double = z
public operator fun Location.component4(): Float = yaw
public operator fun Location.component5(): Float = pitch

public operator fun Block.component1(): Int = x
public operator fun Block.component2(): Int = y
public operator fun Block.component3(): Int = z



public fun Location.dropItem(item: ItemStack): Item = world!!.dropItem(this, item)
public fun Location.dropItemNaturally(item: ItemStack): Item = world!!.dropItemNaturally(this, item)

public fun Location.spawnArrow(direction: Vector, speed: Float, spread: Float): Arrow = world!!.spawnArrow(this, direction, speed, spread)

public fun Location.generateTree(type: TreeType): Boolean = world!!.generateTree(this, type)
public fun Location.generateTree(type: TreeType, delegate: BlockChangeDelegate): Boolean = world!!.generateTree(this, type, delegate)

public fun Location.strikeLightning(): LightningStrike = world!!.strikeLightning(this)
public fun Location.strikeLightningEffect(): LightningStrike = world!!.strikeLightningEffect(this)

public fun Location.getNearbyEntities(x: Double, y: Double, z: Double): MutableCollection<Entity> = world!!.getNearbyEntities(this, x, y, z)

public fun Location.createExplosion(power: Float): Boolean = world!!.createExplosion(this, power)
public fun Location.createExplosion(power: Float, setFire: Boolean): Boolean = world!!.createExplosion(this, power, setFire)

public inline fun <reified T : Entity> Location.spawn(): T = world!!.spawn<T>(this)

public fun Location.playEffect(effect: Effect, data: Int): Unit = world!!.playEffect(this, effect, data)
public fun Location.playEffect(effect: Effect, data: Int, radius: Int): Unit = world!!.playEffect(this, effect, data, radius)
public fun <T> Location.playEffect(effect: Effect, data: T): Unit = world!!.playEffect(this, effect, data)
public fun <T> Location.playEffect(effect: Effect, data: T, radius: Int): Unit = world!!.playEffect(this, effect, data, radius)

public fun Location.playSound(sound: Sound, volume: Float, pitch: Float): Unit = world!!.playSound(this, sound, volume, pitch)