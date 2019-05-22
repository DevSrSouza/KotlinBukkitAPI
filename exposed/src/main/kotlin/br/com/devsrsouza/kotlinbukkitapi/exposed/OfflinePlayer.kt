package br.com.devsrsouza.kotlinbukkitapi.exposed

import br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit.offlinePlayer
import org.bukkit.OfflinePlayer
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import java.util.*
import kotlin.reflect.KProperty

fun Entity<*>.offlinePlayer(column: Column<UUID>) = OfflinePlayerExposedDelegate(column)

class OfflinePlayerExposedDelegate(val column: Column<UUID>) {
    operator fun <ID : Comparable<ID>> getValue(entity: Entity<ID>, desc: KProperty<*>): OfflinePlayer {
        val uuid = entity.run { column.getValue(this, desc) }
        return offlinePlayer(uuid)
    }
    operator fun <ID : Comparable<ID>> setValue(entity: Entity<ID>, desc: KProperty<*>, value: OfflinePlayer) {
        entity.apply { column.setValue(this, desc, value.uniqueId) }
    }
}