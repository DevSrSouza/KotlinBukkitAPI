package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.material.MaterialData

operator fun Location.component1() = x
operator fun Location.component2() = y
operator fun Location.component3() = z

operator fun Block.component1() = x
operator fun Block.component2() = y
operator fun Block.component3() = z

infix fun Block.eqType(block: Block) = typeId == block.typeId && data == block.data
infix fun Block.eqType(data: MaterialData) = typeId == data.itemTypeId && this.data == data.data
infix fun Block.eqType(material: Material) = typeId == material.id