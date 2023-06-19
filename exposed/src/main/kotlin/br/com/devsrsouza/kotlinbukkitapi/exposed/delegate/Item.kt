package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import com.google.gson.annotations.Expose
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.sql.Blob
import kotlin.reflect.KProperty

/**
 * NOTE: This not save NBT and could break version to version, if you pretend
 * to upgrade your server and this keep the same, I can't guarantee that.
 */
public fun Entity<*>.itemStack(column: Column<ExposedBlob>): ExposedDelegate<ItemStack> = ItemStackExposedDelegate(column)
@JvmName("itemStackNullable")
public fun Entity<*>.itemStack(column: Column<ExposedBlob?>): ExposedDelegate<ItemStack?> = ItemStackExposedDelegateNullable(column)

public class ItemStackExposedDelegate(
    public val column: Column<ExposedBlob>
) : ExposedDelegate<ItemStack> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ItemStack {
        val blob = entity.run { column.getValue(this, desc) }
        return toItemStack(blob.bytes)
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ItemStack
    ) {
        val byteArray = toByteArray(value)
        val blob = ExposedBlob(byteArray)
        entity.apply { column.setValue(this, desc, blob) }
    }
}

public class ItemStackExposedDelegateNullable(
    public val column: Column<ExposedBlob?>
) : ExposedDelegate<ItemStack?> {
    override operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): ItemStack? {
        val blob = entity.run { column.getValue(this, desc) }
        return blob?.bytes?.let { toItemStack(it) }
    }

    override operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: ItemStack?
    ) {
        val byteArray = value?.let { toByteArray(it) }
        val blob = byteArray?.let { ExposedBlob(it) }
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