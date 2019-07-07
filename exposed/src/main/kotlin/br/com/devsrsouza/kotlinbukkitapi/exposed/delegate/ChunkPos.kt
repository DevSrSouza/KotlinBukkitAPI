package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import br.com.devsrsouza.kotlinbukkitapi.utils.ChunkPos
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.chunkPos(column: Column<String>) = ChunkPosExposedDelegate(column)
fun Entity<*>.chunkPos(column: Column<String?>) = ChunkPosExposedDelegateNullable(column)

fun Entity<*>.chunkPos(
        xColumn: Column<Int>,
        zColumn: Column<Int>
) = ChunkPosMultiColumnExposedDelegate(xColumn, zColumn)

fun Entity<*>.chunkPos(
        xColumn: Column<Int?>,
        zColumn: Column<Int?>
) = ChunkPosMultiColumnExposedDelegateNullable(xColumn, zColumn)

class ChunkPosExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<ChunkPos> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ChunkPos {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return ChunkPos(
                slices[0].toInt(),
                slices[1].toInt()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ChunkPos
    ) {
        val parsed = value.run { "$x;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class ChunkPosExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<ChunkPos?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ChunkPos? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            ChunkPos(
                    it[0].toInt(),
                    it[1].toInt()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ChunkPos?
    ) {
        val parsed = value?.run { "$x;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class ChunkPosMultiColumnExposedDelegate(
        val xColumn: Column<Int>,
        val zColumn: Column<Int>
) : ExposedDelegate<ChunkPos> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ChunkPos {
        val x = entity.run { xColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return ChunkPos(x, z)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ChunkPos
    ) {
        entity.apply {
            value.apply {
                xColumn.setValue(entity, desc, x)
                zColumn.setValue(entity, desc, z)
            }
        }
    }
}

class ChunkPosMultiColumnExposedDelegateNullable(
        val xColumn: Column<Int?>,
        val zColumn: Column<Int?>
) : ExposedDelegate<ChunkPos?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ChunkPos? {
        val x = entity.run { xColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return if (
                x != null && z != null
        ) ChunkPos(
                x, z
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ChunkPos?
    ) {
        entity.apply {
            xColumn.setValue(entity, desc, value?.x)
            zColumn.setValue(entity, desc, value?.z)
        }
    }
}