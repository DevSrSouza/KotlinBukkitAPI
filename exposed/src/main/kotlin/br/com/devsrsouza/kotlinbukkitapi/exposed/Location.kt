package br.com.devsrsouza.kotlinbukkitapi.exposed

import org.bukkit.Bukkit
import org.bukkit.Location
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.location(column: Column<String>) = LocationExposedDelegate(column)
fun Entity<*>.location(column: Column<String?>) = LocationExposedDelegateNullable(column)

fun Entity<*>.location(
        worldColumn: Column<String>,
        xColumn: Column<Double>,
        yColumn: Column<Double>,
        zColumn: Column<Double>,
        yawColumn: Column<Float>,
        pitchColumn: Column<Float>
) = LocationMultiColumnExposedDelegate(worldColumn, xColumn, yColumn, zColumn, yawColumn, pitchColumn)

fun Entity<*>.location(
        worldColumn: Column<String?>,
        xColumn: Column<Double?>,
        yColumn: Column<Double?>,
        zColumn: Column<Double?>,
        yawColumn: Column<Float?>,
        pitchColumn: Column<Float?>
) = LocationMultiColumnExposedDelegateNullable(worldColumn, xColumn, yColumn, zColumn, yawColumn, pitchColumn)

class LocationExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<Location> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Location {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return Location(
                Bukkit.getWorld(slices[0]),
                slices[1].toDouble(),
                slices[2].toDouble(),
                slices[3].toDouble(),
                slices[4].toFloat(),
                slices[5].toFloat()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Location
    ) {
        val parsed = value.run { "${world.name};$x;$y;$z;$yaw;$pitch" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class LocationExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<Location?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Location? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            Location(
                    Bukkit.getWorld(it[0]),
                    it[1].toDouble(),
                    it[2].toDouble(),
                    it[3].toDouble(),
                    it[4].toFloat(),
                    it[5].toFloat()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Location?
    ) {
        val parsed = value?.run { "${world.name};$x;$y;$z;$yaw;$pitch" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class LocationMultiColumnExposedDelegate(
        val worldColumn: Column<String>,
        val xColumn: Column<Double>,
        val yColumn: Column<Double>,
        val zColumn: Column<Double>,
        val yawColumn: Column<Float>,
        val pitchColumn: Column<Float>
) : ExposedDelegate<Location> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Location {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }
        val yaw = entity.run { yawColumn.getValue(this, desc) }
        val pitch = entity.run { pitchColumn.getValue(this, desc) }

        return Location(
                Bukkit.getWorld(worldName),
                x, y, z, yaw, pitch
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Location
    ) {
        entity.apply {
            value.apply {
                worldColumn.setValue(entity, desc, world.name)
                xColumn.setValue(entity, desc, x)
                yColumn.setValue(entity, desc, y)
                zColumn.setValue(entity, desc, z)
                yawColumn.setValue(entity, desc, yaw)
                pitchColumn.setValue(entity, desc, pitch)
            }
        }
    }
}

class LocationMultiColumnExposedDelegateNullable(
        val worldColumn: Column<String?>,
        val xColumn: Column<Double?>,
        val yColumn: Column<Double?>,
        val zColumn: Column<Double?>,
        val yawColumn: Column<Float?>,
        val pitchColumn: Column<Float?>
) : ExposedDelegate<Location?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): Location? {
        val worldName = entity.run { worldColumn.getValue(this, desc) }
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }
        val yaw = entity.run { yawColumn.getValue(this, desc) }
        val pitch = entity.run { pitchColumn.getValue(this, desc) }

        return if (
                x != null && y != null && z != null &&
                yaw != null && pitch != null
        ) Location(
                Bukkit.getWorld(worldName),
                x, y, z, yaw, pitch
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: Location?
    ) {
        entity.apply {
            worldColumn.setValue(entity, desc, value?.world?.name)
            xColumn.setValue(entity, desc, value?.x)
            yColumn.setValue(entity, desc, value?.y)
            zColumn.setValue(entity, desc, value?.z)
            yawColumn.setValue(entity, desc, value?.yaw)
            pitchColumn.setValue(entity, desc, value?.pitch)
        }
    }
}