package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import org.bukkit.Bukkit
import org.bukkit.World
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import kotlin.reflect.KProperty

public fun Entity<*>.world(column: Column<String>): ExposedDelegate<World> = WorldExposedDelegate(column)
@JvmName("worldNullable")
public fun Entity<*>.world(column: Column<String?>): ExposedDelegate<World?> = WorldExposedDelegateNullable(column)

public class WorldExposedDelegate(
    public val column: Column<String>
) : ExposedDelegate<World> {
    override operator fun <ID : Comparable<ID>> getValue(
        entity: Entity<ID>,
        desc: KProperty<*>
    ): World {
        val data = entity.run { column.getValue(this, desc) }
        return requireNotNull(Bukkit.getWorld(data)) { "World '$data' retrieving from database not loaded at server." }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: World
    ) {
        entity.apply { column.setValue(this, desc, value.name) }
    }
}

public class WorldExposedDelegateNullable(
    public val column: Column<String?>
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