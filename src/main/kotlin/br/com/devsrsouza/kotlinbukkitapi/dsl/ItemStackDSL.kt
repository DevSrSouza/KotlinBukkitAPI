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
import com.google.common.collect.Multimap
import java.io.ByteArrayInputStream
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.MaterialData

inline fun <T : ItemMeta> ItemStack.meta(block: T.() -> Unit) = apply {
    itemMeta = (itemMeta as T).apply(block)
}

fun Material.asItemStack(amount: Int = 1, data: Short = 0) = ItemStack(this, amount, data)
fun Material.asMaterialData(data: Byte = 0) = MaterialData(this, data)

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

/**
 * get head from base64
 */
val gameProfileClass by lazy { Class.forName("com.mojang.authlib.GameProfile") }
val propertyClass by lazy { Class.forName("com.mojang.authlib.properties.Property") }
val getPropertiesMethod by lazy { gameProfileClass.getMethod("getProperties") }
val gameProfileConstructor by lazy { gameProfileClass.getConstructor(UUID::class.java, String::class.java) }
val propertyConstructor by lazy { propertyClass.getConstructor(String::class.java, String::class.java) }

fun headFromBase64(base64: String): ItemStack {
    val item = ItemStack(Material.SKULL_ITEM, 1, 3)
    val meta = item.itemMeta as SkullMeta

    val profile = gameProfileConstructor.newInstance(UUID.randomUUID(), null as String?)
    val properties = getPropertiesMethod.invoke(profile) as Multimap<Any, Any>
    properties.put("textures", propertyConstructor.newInstance("textures", base64))

    val profileField = meta.javaClass.getDeclaredField("profile").apply { isAccessible = true }
    profileField.set(meta, profile)

    return item.apply { itemMeta = meta }
}