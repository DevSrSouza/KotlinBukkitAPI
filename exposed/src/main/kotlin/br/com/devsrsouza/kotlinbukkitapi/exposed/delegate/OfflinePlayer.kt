package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import br.com.devsrsouza.kotlinbukkitapi.extensions.offlinePlayer
import org.bukkit.OfflinePlayer
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import java.util.*
import kotlin.reflect.KProperty

public fun Entity<*>.offlinePlayer(column: Column<UUID>): ExposedDelegate<OfflinePlayer> = OfflinePlayerExposedDelegate(column)
@JvmName("offlinePlayerNullable")
public fun Entity<*>.offlinePlayer(column: Column<UUID?>): ExposedDelegate<OfflinePlayer?> = OfflinePlayerExposedDelegateNullable(column)

public class OfflinePlayerExposedDelegate(
    public val column: Column<UUID>
) : ExposedDelegate<OfflinePlayer> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): OfflinePlayer {
        val uuid = entity.run { column.getValue(this, desc) }
        return offlinePlayer(uuid)
    }
    override operator fun <ID : Comparable<ID>> setValue(entity: Entity<ID>, desc: KProperty<*>, value: OfflinePlayer) {
        entity.apply { column.setValue(this, desc, value.uniqueId) }
    }
}

public class OfflinePlayerExposedDelegateNullable(
    public val column: Column<UUID?>
) : ExposedDelegate<OfflinePlayer?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): OfflinePlayer? {
        val uuid = entity.run { column.getValue(this, desc) }
        return uuid?.let { offlinePlayer(it) }
    }
    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: OfflinePlayer?
    ) {
        entity.apply { column.setValue(this, desc, value?.uniqueId) }
    }
}