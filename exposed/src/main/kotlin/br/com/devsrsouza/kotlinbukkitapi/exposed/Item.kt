package br.com.devsrsouza.kotlinbukkitapi.exposed

import br.com.devsrsouza.kotlinbukkitapi.server.extensions.itemFromByteArray
import br.com.devsrsouza.kotlinbukkitapi.server.extensions.toByteArray
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob
import kotlin.reflect.KProperty

fun Entity<*>.itemStack(column: Column<Blob>) = ItemStackExposedDelegate(column)

class ItemStackExposedDelegate(val column: Column<Blob>) {
    operator fun <ID : Comparable<ID>> getValue(entity: Entity<ID>, desc: KProperty<*>): ItemStack {
        val blob = entity.run { column.getValue(this, desc) }
        val byteArray = blob.getBytes(1, blob.length().toInt())
        return itemFromByteArray(byteArray)
    }

    operator fun <ID : Comparable<ID>> setValue(entity: Entity<ID>, desc: KProperty<*>, value: ItemStack) {
        val byteArray = value.toByteArray()
        val blob = SerialBlob(byteArray)
        entity.apply { column.setValue(this, desc, blob) }
    }
}