package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.block(column: Column<String>) = BlockExposedDelegate(column)
fun Entity<*>.block(column: Column<String?>) = BlockExposedDelegateNullable(column)

fun Entity<*>.block(
        worldColumn: Column<String>,
        xColumn: Column<Int>,
        yColumn: Column<Int>,
        zColumn: Column<Int>
) = BlockMultiColumnExposedDelegate(worldColumn, xColumn, yColumn, zColumn)

fun Entity<*>.block(
        worldColumn: Column<String?>,
        xColumn: Column<Int?>,
        yColumn: Column<Int?>,
        zColumn: Column<Int?>
) = BlockMultiColumnExposedDelegateNullable(worldColumn, xColumn, yColumn, zColumn)

class BlockExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<Block> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return Bukkit.getWorld(slices[0]).getBlockAt(
                slices[1].toInt(),
                slices[2].toInt(),
                slices[3].toInt()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Block
    ) {
        val parsed = value.run { "${world.name};$x;$y;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class BlockExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<Block?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            Bukkit.getWorld(it[0]).getBlockAt(
                    it[1].toInt(),
                    it[2].toInt(),
                    it[3].toInt()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Block?
    ) {
        val parsed = value?.run { "${world.name};$x;$y;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class BlockMultiColumnExposedDelegate(
        val worldColumn: Column<String>,
        val xColumn: Column<Int>,
        val yColumn: Column<Int>,
        val zColumn: Column<Int>
) : ExposedDelegate<Block> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return Bukkit.getWorld(worldName).getBlockAt(x, y, z)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Block
    ) {
        entity.apply {
            value.apply {
                worldColumn.setValue(entity, desc, world.name)
                xColumn.setValue(entity, desc, x)
                yColumn.setValue(entity, desc, y)
                zColumn.setValue(entity, desc, z)
            }
        }
    }
}

class BlockMultiColumnExposedDelegateNullable(
        val worldColumn: Column<String?>,
        val xColumn: Column<Int?>,
        val yColumn: Column<Int?>,
        val zColumn: Column<Int?>
) : ExposedDelegate<Block?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block? {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return if (
                worldName != null &&
                x != null && y != null && z != null
        ) Bukkit.getWorld(worldName).getBlockAt(
                x, y, z
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Block?
    ) {
        entity.apply {
            worldColumn.setValue(entity, desc, value?.world?.name)
            xColumn.setValue(entity, desc, value?.x)
            yColumn.setValue(entity, desc, value?.y)
            zColumn.setValue(entity, desc, value?.z)
        }
    }
}