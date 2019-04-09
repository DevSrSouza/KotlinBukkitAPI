package br.com.devsrsouza.kotlinbukkitapi.attributestorage

import br.com.devsrsouza.kotlinbukkitapi.config.Serializable
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.meta
import com.comphenix.attribute.AttributeStorage
import com.comphenix.attribute.NbtFactory
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

fun ItemStack.setStorageData(data: String, key: UUID): ItemStack {
    return AttributeStorage.newTarget(this, key).apply {
        setData(data)
    }.run {
        target.meta<ItemMeta> {
            addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }
}

fun ItemStack.getStorageData(key: UUID): String? {
    return AttributeStorage.newTarget(this, key).run {
        getData(null)
    }
}

fun ItemStack.toByteArray(): ByteArray {
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

    NbtFactory.saveStream(nbt, { output }, NbtFactory.StreamOptions.GZIP_COMPRESSION)

    return output.toByteArray()
}

fun ItemStack.toBase64(): String = Base64Coder.encodeLines(toByteArray())

fun fromByteArrayItem(byteArray: ByteArray): ItemStack {
    val nbt = NbtFactory.fromStream(
            { ByteArrayInputStream(byteArray) },
            NbtFactory.StreamOptions.GZIP_COMPRESSION
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

fun fromBase64Item(data: String): ItemStack = fromByteArrayItem(Base64Coder.decodeLines(data))

// serializers

fun itemBase64Serializer(item: ItemStack, description: String = "")
        = Serializable(item, description).apply {
    load { (it as? String)?.let { fromBase64Item(it) } ?: default }
    save { toBase64() }
}

fun itemBase64ListSerializer(items: MutableList<ItemStack>, description: String = "")
        = Serializable(items, description).apply {
    load {
        (it as? List<String>)?.mapNotNull {
            runCatching { fromBase64Item(it) }.getOrNull()
        }?.toMutableList() ?: default
    }
    save { map { it.toBase64() } }
}

