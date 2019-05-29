package br.com.devsrsouza.kotlinbukkitapi.exposed

import org.bukkit.Bukkit
import org.bukkit.World
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

fun Entity<*>.world(column: Column<String>) = WorldExposedDelegate(column)
fun Entity<*>.world(column: Column<String?>) = WorldExposedDelegateNullable(column)

class WorldExposedDelegate(
        val column: Column<String>
) : ExposedDelegate<World> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): World {
        val data = entity.run { column.getValue(this, desc) }
        return Bukkit.getWorld(data)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: World
    ) {
        entity.apply { column.setValue(this, desc, value.name) }
    }
}

class WorldExposedDelegateNullable(
        val column: Column<String?>
) : ExposedDelegate<World?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): World? {
        val data = entity.run { column.getValue(this, desc) }
        return data?.let { Bukkit.getWorld(it) }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: World?
    ) {
        entity.apply { column.setValue(this, desc, value?.name) }
    }
}