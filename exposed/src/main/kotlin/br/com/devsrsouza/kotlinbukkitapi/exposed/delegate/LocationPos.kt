package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import br.com.devsrsouza.kotlinbukkitapi.utils.LocationPos
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.locationPos(column: Column<String>) = LocationPosExposedDelegate(column)
fun Entity<*>.nullableLocationPos(column: Column<String?>) = LocationPosExposedDelegateNullable(column)

fun Entity<*>.locationPos(
        xColumn: Column<Double>,
        yColumn: Column<Double>,
        zColumn: Column<Double>,
        yawColumn: Column<Float>,
        pitchColumn: Column<Float>
) = LocationPosMultiColumnExposedDelegate(xColumn, yColumn, zColumn, yawColumn, pitchColumn)

fun Entity<*>.locationPos(
        xColumn: Column<Double?>,
        yColumn: Column<Double?>,
        zColumn: Column<Double?>,
        yawColumn: Column<Float?>,
        pitchColumn: Column<Float?>
) = LocationPosMultiColumnExposedDelegateNullable(xColumn, yColumn, zColumn, yawColumn, pitchColumn)

class LocationPosExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<LocationPos> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): LocationPos {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data.split(";")
        return LocationPos(
                slices[0].toDouble(),
                slices[1].toDouble(),
                slices[2].toDouble(),
                slices[3].toFloat(),
                slices[4].toFloat()
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: LocationPos
    ) {
        val parsed = value.run { "$x;$y;$z;$yaw;$pitch" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class LocationPosExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<LocationPos?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): LocationPos? {
        val data = entity.run { column.getValue(this, desc) }
        val slices = data?.split(";")
        return slices?.let {
            LocationPos(
                    it[0].toDouble(),
                    it[1].toDouble(),
                    it[2].toDouble(),
                    it[3].toFloat(),
                    it[4].toFloat()
            )
        }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: LocationPos?
    ) {
        val parsed = value?.run { "$x;$y;$z;$yaw;$pitch" }
        entity.apply { column.setValue(this, desc, parsed) }
    }
}

class LocationPosMultiColumnExposedDelegate(
        val xColumn: Column<Double>,
        val yColumn: Column<Double>,
        val zColumn: Column<Double>,
        val yawColumn: Column<Float>,
        val pitchColumn: Column<Float>
) : ExposedDelegate<LocationPos> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): LocationPos {
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }
        val yaw = entity.run { yawColumn.getValue(this, desc) }
        val pitch = entity.run { pitchColumn.getValue(this, desc) }

        return LocationPos(
                x, y, z, yaw, pitch
        )
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: LocationPos
    ) {
        entity.apply {
            value.apply {
                xColumn.setValue(entity, desc, x)
                yColumn.setValue(entity, desc, y)
                zColumn.setValue(entity, desc, z)
                yawColumn.setValue(entity, desc, yaw)
                pitchColumn.setValue(entity, desc, pitch)
            }
        }
    }
}

class LocationPosMultiColumnExposedDelegateNullable(
        val xColumn: Column<Double?>,
        val yColumn: Column<Double?>,
        val zColumn: Column<Double?>,
        val yawColumn: Column<Float?>,
        val pitchColumn: Column<Float?>
) : ExposedDelegate<LocationPos?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): LocationPos? {
        val x = entity.run { xColumn.getValue(this, desc) }
        val y = entity.run { yColumn.getValue(this, desc) }
        val z = entity.run { zColumn.getValue(this, desc) }
        val yaw = entity.run { yawColumn.getValue(this, desc) }
        val pitch = entity.run { pitchColumn.getValue(this, desc) }

        return if (
                x != null && y != null && z != null &&
                yaw != null && pitch != null
        ) LocationPos(
                x, y, z, yaw, pitch
        ) else null
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: LocationPos?
    ) {
        entity.apply {
            xColumn.setValue(entity, desc, value?.x)
            yColumn.setValue(entity, desc, value?.y)
            zColumn.setValue(entity, desc, value?.z)
            yawColumn.setValue(entity, desc, value?.yaw)
            pitchColumn.setValue(entity, desc, value?.pitch)
        }
    }
}