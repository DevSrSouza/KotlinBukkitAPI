package br.com.devsrsouza.kotlinbukkitapi.dsl.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*
import com.google.common.collect.Multimap
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.MaterialData

inline fun <T : ItemMeta> ItemStack.meta(block: T.() -> Unit) = apply {
    itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta
}

fun Material.asItemStack(amount: Int = 1, data: Short = 0) = ItemStack(this, amount, data)
fun Material.asMaterialData(data: Byte = 0) = MaterialData(this, data)

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