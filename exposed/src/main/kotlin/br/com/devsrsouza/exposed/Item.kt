package br.com.devsrsouza.exposed

import br.com.devsrsouza.kotlinbukkitapi.attributestorage.fromByteArrayItem
import br.com.devsrsouza.kotlinbukkitapi.attributestorage.toByteArray
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.io.InputStream
import java.sql.Blob

fun Table.itemStack(name: String) = registerColumn<ItemStack>(name, ItemStackColumn())

class ItemStackColumn : ColumnType() { // from BlobColumnType

    private val currentDialect get() = TransactionManager.current().db.dialect

    override fun sqlType(): String  = currentDialect.dataTypeProvider.blobType()

    override fun nonNullValueToString(value: Any): String = when (value) {
        is ItemStack -> value.toString()
        else -> "?"
    }

    override fun valueFromDB(value: Any): Any = when (value) {
        is ItemStack -> value
        is ByteArray -> fromByteArrayItem(value)
        is Blob -> fromByteArrayItem(value.getBytes(1, value.length().toInt()))
        is InputStream -> fromByteArrayItem(value.readBytes())
        else -> error("Unknown type for blob (ItemStack) column: ${value.javaClass}")
    }

    override fun notNullValueToDB(value: Any): Any {
        return when (value) {
            is ItemStack -> value.toByteArray()
            else -> error("Unexpected value of type ItemStack: ${value.javaClass.canonicalName}")
        }
    }
}