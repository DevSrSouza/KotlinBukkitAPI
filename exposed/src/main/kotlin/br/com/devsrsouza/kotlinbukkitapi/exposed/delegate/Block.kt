package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

public fun Entity<*>.block(column: Column<String>): ExposedDelegate<Block> = BlockExposedDelegate(column)
@JvmName("blockNullable")
public fun Entity<*>.block(column: Column<String?>): ExposedDelegate<Block?> = BlockExposedDelegateNullable(column)

public fun Entity<*>.block(
        worldColumn: Column<String>,
        xColumn: Column<Int>,
        yColumn: Column<Int>,
        zColumn: Column<Int>
): ExposedDelegate<Block> = BlockMultiColumnExposedDelegate(worldColumn, xColumn, yColumn, zColumn)

@JvmName("blockNullable")
public fun Entity<*>.nullableBlock(
        worldColumn: Column<String?>,
        xColumn: Column<Int?>,
        yColumn: Column<Int?>,
        zColumn: Column<Int?>
): ExposedDelegate<Block?> = BlockMultiColumnExposedDelegateNullable(worldColumn, xColumn, yColumn, zColumn)

public class BlockExposedDelegate(
    public val column: Column<String>
) : ExposedDelegate<Block> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        val worldName = slices[0]
        val world = requireNotNull(Bukkit.getWorld(worldName)) { "World '$worldName' retrieving from database unavailable." }
        return world.getBlockAt(
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

public class BlockExposedDelegateNullable(
    public val column: Column<String?>
) : ExposedDelegate<Block?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            val worldName = it[0]
            val world = requireNotNull(Bukkit.getWorld(worldName)) { "World '$worldName' retrieving from database unavailable." }
            world.getBlockAt(
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

public class BlockMultiColumnExposedDelegate(
    public val worldColumn: Column<String>,
    public val xColumn: Column<Int>,
    public val yColumn: Column<Int>,
    public val zColumn: Column<Int>
) : ExposedDelegate<Block> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Block {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }

        val world = requireNotNull(Bukkit.getWorld(worldName)) { "World '$worldName' retrieving from database unavailable." }

        return world.getBlockAt(x, y, z)
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

public class BlockMultiColumnExposedDelegateNullable(
    public val worldColumn: Column<String?>,
    public val xColumn: Column<Int?>,
    public val yColumn: Column<Int?>,
    public val zColumn: Column<Int?>
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
        ) {
            val world = requireNotNull(Bukkit.getWorld(worldName)) { "World '$worldName' retrieving from database unavailable." }

            world.getBlockAt(x, y, z)
        } else {
            null
        }
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