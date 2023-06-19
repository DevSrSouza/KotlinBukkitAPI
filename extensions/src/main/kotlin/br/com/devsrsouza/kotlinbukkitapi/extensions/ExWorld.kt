package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.material.MaterialData

public fun mainWorld(): World = Bukkit.getWorlds()[0]
public fun chunk(world: World, x: Int, y: Int): Chunk = world.getChunkAt(x, y)
public fun chunk(block: Block): Chunk = chunk(block.world, block.x shr 4, block.z shr 4)

public inline fun <reified T : Entity> World.spawn(location: Location): T {
    return spawn(location, T::class.java)
}

public inline fun <reified T : Entity> World.getEntitiesByClass(): Collection<T> {
    return getEntitiesByClass(T::class.java)
}

public fun World.dropItem(location: Location, material: Material, data: Byte = 0): Item {
    return dropItem(location, material.asMaterialData(data))
}

public fun World.dropItem(location: Location, materialData: MaterialData): Item {
    return dropItem(location, materialData.toItemStack(1))
}

public fun World.dropItemNaturally(location: Location, material: Material, data: Byte = 0): Item {
    return dropItemNaturally(location, material.asMaterialData(data))
}

public fun World.dropItemNaturally(location: Location, materialData: MaterialData): Item {
    return dropItemNaturally(location, materialData.toItemStack(1))
}

public fun World.setSpawnLocation(location: Location): Boolean {
    return setSpawnLocation(location.blockX, location.blockY, location.blockZ)
}

public fun World.setSpawnLocation(block: Block): Boolean {
    return setSpawnLocation(block.x, block.y, block.z)
}
