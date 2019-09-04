package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.material.MaterialData

fun Block.sendBlockChange(
        materialData: MaterialData,
        players: List<Player>
) = sendBlockChange(materialData.itemType, materialData.data, players)

fun Block.sendBlockChange(
        material: Material,
        data: Byte = 0,
        players: List<Player>
) {
    players.filter { it.world.name == world.name }.forEach {
        it.sendBlockChange(location, material, data)
    }
}