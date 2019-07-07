package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import br.com.devsrsouza.kotlinbukkitapi.utils.BlockPos
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.blockPos(column: Column<String>) = BlockPosExposedDelegate(column)
fun Entity<*>.blockPos(column: Column<String?>) = BlockPosExposedDelegateNullable(column)

fun Entity<*>.blockPos(
        xColumn: Column<Int>,
        yColumn: Column<Int>,
        zColumn: Column<Int>
) = BlockPosMultiColumnExposedDelegate(xColumn, yColumn, zColumn)

fun Entity<*>.blockPos(
        xColumn: Column<Int?>,
        yColumn: Column<Int?>,
        zColumn: Column<Int?>
) = BlockPosMultiColumnExposedDelegateNullable(xColumn, yColumn, zColumn)

class BlockPosExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<BlockPos> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): BlockPos {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return BlockPos(
                slices[0].toInt(),
                slices[1].toInt(),
                slices[2].toInt()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: BlockPos
    ) {
        val parsed = value.run { "$x;$y;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class BlockPosExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<BlockPos?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): BlockPos? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            BlockPos(
                    it[0].toInt(),
                    it[1].toInt(),
                    it[2].toInt()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: BlockPos?
    ) {
        val parsed = value?.run { "$x;$y;$z" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class BlockPosMultiColumnExposedDelegate(
        val xColumn: Column<Int>,
        val yColumn: Column<Int>,
        val zColumn: Column<Int>
) : ExposedDelegate<BlockPos> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): BlockPos {
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return BlockPos(x, y, z)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: BlockPos
    ) {
        entity.apply {
            value.apply {
                xColumn.setValue(entity, desc, x)
                yColumn.setValue(entity, desc, y)
                zColumn.setValue(entity, desc, z)
            }
        }
    }
}

class BlockPosMultiColumnExposedDelegateNullable(
        val xColumn: Column<Int?>,
        val yColumn: Column<Int?>,
        val zColumn: Column<Int?>
) : ExposedDelegate<BlockPos?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): BlockPos? {
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        return if (
                x != null && y != null && z != null
        ) BlockPos(
                x, y, z
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: BlockPos?
    ) {
        entity.apply {
            xColumn.setValue(entity, desc, value?.x)
            yColumn.setValue(entity, desc, value?.y)
            zColumn.setValue(entity, desc, value?.z)
        }
    }
}