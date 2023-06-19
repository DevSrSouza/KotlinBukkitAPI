package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.material.MaterialData

public infix fun Block.eqType(block: Block): Boolean = type == block.type && data == block.data
public infix fun Block.eqType(data: MaterialData): Boolean = type == data.itemType && this.data == data.data
public infix fun Block.eqType(material: Material): Boolean = type == material

public fun Block.sendBlockChange(
    materialData: MaterialData,
    players: List<Player>,
): Unit = sendBlockChange(materialData.itemType, materialData.data, players)

public fun Block.sendBlockChange(
    material: Material,
    data: Byte = 0,
    players: List<Player>,
) {
    players.filter { it.world.name == world.name }.forEach {
        it.sendBlockChange(location, material, data)
    }
}
