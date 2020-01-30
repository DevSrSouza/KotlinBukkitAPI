package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob
import kotlin.reflect.KProperty

/**
 * NOTE: This not save NBT and could break version to version, if you pretend
 * to upgrade your server and this keep the same, I can't guarantee that.
 */
fun Entity<*>.itemStack(column: Column<Blob>) = ItemStackExposedDelegate(column)
fun Entity<*>.nullableItemStack(column: Column<Blob?>) = ItemStackExposedDelegateNullable(column)

class ItemStackExposedDelegate(
        val column: Column<Blob>
) : ExposedDelegate<ItemStack> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ItemStack {
        val blob = entity.run { column.getValue(this, desc) }
        val byteArray = blob.getBytes(1, blob.length().toInt())
        return toItemStack(byteArray)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ItemStack
    ) {
        val byteArray = toByteArray(value)
        val blob = SerialBlob(byteArray)
        entity.apply { column.setValue(this, desc, blob) }
    }
}

class ItemStackExposedDelegateNullable(
        val column: Column<Blob?>
) : ExposedDelegate<ItemStack?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ItemStack? {
        val blob = entity.run { column.getValue(this, desc) }
        val byteArray = blob?.getBytes(1, blob.length().toInt())
        return byteArray?.let { toItemStack(it) }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ItemStack?
    ) {
        val byteArray = value?.let { toByteArray(it) }
        val blob = byteArray?.let { SerialBlob(it) }
        entity.apply { column.setValue(this, desc, blob) }
    }
}

private fun toByteArray(item: ItemStack): ByteArray {
    val outputStream = ByteArrayOutputStream()

    BukkitObjectOutputStream(outputStream).use {
        it.writeObject(item)
    }

    return outputStream.toByteArray()
}

private fun toItemStack(byteArray: ByteArray): ItemStack {
    return ByteArrayInputStream(byteArray).use {
        BukkitObjectInputStream(it).use {
            it.readObject() as ItemStack
        }
    }
}