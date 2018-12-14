package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import org.bukkit.util.Vector

operator fun Location.component1() = x
operator fun Location.component2() = y
operator fun Location.component3() = z
operator fun Location.component4() = yaw
operator fun Location.component5() = pitch

operator fun Block.component1() = x
operator fun Block.component2() = y
operator fun Block.component3() = z

infix fun Block.eqType(block: Block) = typeId == block.typeId && data == block.data
infix fun Block.eqType(data: MaterialData) = typeId == data.itemTypeId && this.data == data.data
infix fun Block.eqType(material: Material) = typeId == material.id

fun Location.dropItem(item: ItemStack) = world.dropItem(this, item)
fun Location.dropItemNaturally(item: ItemStack) = world.dropItemNaturally(this, item)

fun Location.spawnArrow(direction: Vector, speed: Float, spread: Float) = world.spawnArrow(this, direction, speed, spread)

fun Location.generateTree(type: TreeType) = world.generateTree(this, type)
fun Location.generateTree(type: TreeType, delegate: BlockChangeDelegate) = world.generateTree(this, type, delegate)

fun Location.strikeLightning() = world.strikeLightning(this)
fun Location.strikeLightningEffect() = world.strikeLightningEffect(this)

fun Location.getNearbyEntities(x: Double, y: Double, z: Double) = world.getNearbyEntities(this, x, y, z)

fun Location.createExplosion(power: Float) = world.createExplosion(this, power)
fun Location.createExplosion(power: Float, setFire: Boolean) = world.createExplosion(this, power, setFire)

inline fun <reified T : Entity> Location.spawn() = world.spawn<T>(this)

fun Location.playEffect(effect: Effect, data: Int) = world.playEffect(this, effect, data)
fun Location.playEffect(effect: Effect, data: Int, radius: Int) = world.playEffect(this, effect, data, radius)
fun <T> Location.playEffect(effect: Effect, data: T) = world.playEffect(this, effect, data)
fun <T> Location.playEffect(effect: Effect, data: T, radius: Int) = world.playEffect(this, effect, data, radius)

fun Location.playSound(sound: Sound, volume: Float, pitch: Float) = world.playSound(this, sound, volume, pitch)