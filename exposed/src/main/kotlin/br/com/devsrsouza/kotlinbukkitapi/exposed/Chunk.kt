package br.com.devsrsouza.kotlinbukkitapi.exposed

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.chunk(column: Column<String>) = ChunkExposedDelegate(column)
fun Entity<*>.chunk(column: Column<String?>) = ChunkExposedDelegateNullable(column)

fun Entity<*>.chunk(
        worldColumn: Column<String>,
        xColumn: Column<Int>,
        zColumn: Column<Int>
) = ChunkMultiColumnExposedDelegate(worldColumn, xColumn, zColumn)

fun Entity<*>.chunk(
        worldColumn: Column<String?>,
        xColumn: Column<Int?>,
        zColumn: Column<Int?>
) = ChunkMultiColumnExposedDelegateNullable(worldColumn, xColumn, zColumn)

class ChunkExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<Chunk> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Chunk {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return Bukkit.getWorld(slices[0]).getChunkAt(
                slices[1].toInt(),
                slices[2].toInt()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Chunk
    ) {
        val parsed = value.run { "${world.name};$x;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class ChunkExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<Chunk?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Chunk? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            Bukkit.getWorld(it[0]).getChunkAt(
                    it[1].toInt(),
                    it[2].toInt()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Chunk?
    ) {
        val parsed = value?.run { "${world.name};$x;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class ChunkMultiColumnExposedDelegate(
        val worldColumn: Column<String>,
        val xColumn: Column<Int>,
        val zColumn: Column<Int>
) : ExposedDelegate<Chunk> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Chunk {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return Bukkit.getWorld(worldName).getChunkAt(x, z)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Chunk
    ) {
        entity.apply {
            value.apply {
                worldColumn.setValue(entity, desc, world.name)
                xColumn.setValue(entity, desc, x)
                zColumn.setValue(entity, desc, z)
            }
        }
    }
}

class ChunkMultiColumnExposedDelegateNullable(
        val worldColumn: Column<String?>,
        val xColumn: Column<Int?>,
        val zColumn: Column<Int?>
) : ExposedDelegate<Chunk?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Chunk? {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return if (
                x != null && z != null
        ) Bukkit.getWorld(worldName).getChunkAt(
                x, z
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Chunk?
    ) {
        entity.apply {
            worldColumn.setValue(entity, desc, value?.world?.name)
            xColumn.setValue(entity, desc, value?.x)
            zColumn.setValue(entity, desc, value?.z)
        }
    }
}