package br.com.devsrsouza.kotlinbukkitapi.dsl.item

import com.comphenix.attribute.AttributeStorage
import com.comphenix.attribute.NbtFactory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayOutputStream
import com.comphenix.attribute.NbtFactory.StreamOptions
import java.io.ByteArrayInputStream
import org.bukkit.Material

inline fun <T : ItemMeta> ItemStack.meta(block: T.() -> Unit) {
    itemMeta = (itemMeta as T).apply(block)
}

fun ItemStack.setStorageData(data: String, key: UUID): ItemStack {
    return AttributeStorage.newTarget(this, key).apply {
        setData(data)
    }.run {
        target.apply {
            meta<ItemMeta> { addItemFlags(ItemFlag.HIDE_ATTRIBUTES) }
        }
    }
}

fun ItemStack.getStorageData(key: UUID): String? {
    return AttributeStorage.newTarget(this, key).run {
        getData(null)
    }
}

fun ItemStack.toBase64(): String {

    val craftItemStack = NbtFactory.getCraftItemStack(this)

    val nbt = NbtFactory.createCompound().apply {
        put("id", typeId)
        put("data", durability)
        put("count", amount)

        NbtFactory.fromItemTag(craftItemStack)?.also {
            put("tag", it)
        }
    }

    val output = ByteArrayOutputStream()

    NbtFactory.saveStream(nbt, {output}, NbtFactory.StreamOptions.GZIP_COMPRESSION)

    return Base64Coder.encodeLines(output.toByteArray())
}

fun fromBase64Item(data: String): ItemStack {

    val nbt = NbtFactory.fromStream(
            {ByteArrayInputStream(Base64Coder.decodeLines(data))},
            StreamOptions.GZIP_COMPRESSION
    )
    var stack = ItemStack(
            Material.getMaterial(nbt.getInteger("id", 0)),
            nbt.getInteger("count", 0),
            nbt.getShort("data", 0.toShort())
    )

    if (nbt.containsKey("tag")) {
        stack = NbtFactory.getCraftItemStack(stack)
        NbtFactory.setItemTag(stack, nbt.getMap("tag", false))
    }

    return stack
}