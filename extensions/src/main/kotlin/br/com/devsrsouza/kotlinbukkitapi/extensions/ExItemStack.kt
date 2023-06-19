package br.com.devsrsouza.kotlinbukkitapi.extensions

import com.google.common.collect.Multimap
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.MaterialData
import java.util.*

public inline fun item(
    material: Material,
    amount: Int = 1,
    data: Short = 0,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = ItemStack(material, amount, data).meta(meta)

public inline fun <reified T : ItemMeta> metadataItem(
    material: Material,
    amount: Int = 1,
    data: Short = 0,
    meta: T.() -> Unit,
): ItemStack = ItemStack(material, amount, data).meta(meta)

public inline fun <reified T : ItemMeta> ItemStack.meta(
    block: T.() -> Unit,
): ItemStack = apply {
    itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta
}

public fun ItemStack.displayName(displayName: String?): ItemStack = meta<ItemMeta> {
    this.setDisplayName(displayName)
}

public fun ItemStack.lore(lore: List<String>): ItemStack = meta<ItemMeta> {
    this.lore = lore
}

public inline fun Material.asItemStack(
    amount: Int = 1,
    data: Short = 0,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = item(this, amount, data, meta)

public fun Material.asMaterialData(
    data: Byte = 0,
): MaterialData = MaterialData(this, data)

public fun MaterialData.toItemStack(
    amount: Int = 1,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = toItemStack(amount).meta(meta)

/**
 * get head from base64
 */
private val gameProfileClass by lazy { Class.forName("com.mojang.authlib.GameProfile") }
private val propertyClass by lazy { Class.forName("com.mojang.authlib.properties.Property") }
private val getPropertiesMethod by lazy { gameProfileClass.getMethod("getProperties") }
private val gameProfileConstructor by lazy { gameProfileClass.getConstructor(UUID::class.java, String::class.java) }
private val propertyConstructor by lazy { propertyClass.getConstructor(String::class.java, String::class.java) }

public fun headFromBase64(base64: String): ItemStack {
    val item = ItemStack(Material.LEGACY_SKULL_ITEM, 1, 3)
    val meta = item.itemMeta as SkullMeta

    val profile = gameProfileConstructor.newInstance(UUID.randomUUID(), null as String?)
    val properties = getPropertiesMethod.invoke(profile) as Multimap<Any, Any>
    properties.put("textures", propertyConstructor.newInstance("textures", base64))

    val profileField = meta.javaClass.getDeclaredField("profile").apply { isAccessible = true }
    profileField.set(meta, profile)

    return item.apply { itemMeta = meta }
}

public inline val Material.isPickaxe: Boolean get() = name.endsWith("PICKAXE")
public inline val Material.isSword: Boolean get() = name.endsWith("SWORD")
public inline val Material.isAxe: Boolean get() = name.endsWith("_AXE")
public inline val Material.isSpade: Boolean get() = name.endsWith("SPADE")
public inline val Material.isHoe: Boolean get() = name.endsWith("HOE")
public inline val Material.isOre: Boolean get() = name.endsWith("ORE")
public inline val Material.isIngot: Boolean get() = name.endsWith("INGOT")
public inline val Material.isDoor: Boolean get() = name.endsWith("DOOR")
public inline val Material.isMinecart: Boolean get() = name.endsWith("MINECART")
public inline val Material.isWater: Boolean get() = this == Material.WATER || this == Material.LEGACY_STATIONARY_WATER
public inline val Material.isLava: Boolean get() = this == Material.LAVA || this == Material.LEGACY_STATIONARY_LAVA
