package br.com.devsrsouza.kotlinbukkitapi.server.extensions

import br.com.devsrsouza.kotlinbukkitapi.extensions.item.item
import br.com.devsrsouza.kotlinbukkitapi.server.Server
import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.io.byteArrayToTag
import br.com.devsrsouza.kotlinnbt.api.io.toByteArray
import br.com.devsrsouza.kotlinnbt.api.tags.CompoundTag
import br.com.devsrsouza.kotlinnbt.api.tags.nbtCompound
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

val ItemStack.nbt: CompoundTag get() = Server.item.getTag(this)

fun ItemStack.nbt(tag: CompoundTag) = Server.item.setTag(this, tag)

fun ItemStack.addNBTTag(key: String, tag: ITag) = Server.item.addTag(this, key, tag)

fun ItemStack.removeNBTTag(key: String) = Server.item.removeTag(this, key)

fun SkullMeta.setHeadFromJson(json: String) {
    Server.item.setHeadFromJson(this, json)
}

fun SkullMeta.setHeadFromUrl(url: String) {
    Server.item.setHeadFromUrl(this, url)
}

fun ItemStack.serializeToNBT(): CompoundTag = nbtCompound {
    int["id"] = typeId // TODO update to tag (minecraft:item)
    short["data"] = durability
    int["count"] = amount

    put("tag", nbt)
}

fun itemFromCompoundOrNull(compound: CompoundTag): ItemStack? = runCatching {
    compound.run {
        item(Material.getMaterial(int["id"]!!), int["count"]!!, short["data"]!!).run {
            nbt(get("tag")!! as CompoundTag)
        }
    }
}.getOrNull()

fun itemFromCompound(compound: CompoundTag): ItemStack = itemFromCompoundOrNull(compound)
        ?: throw IllegalArgumentException("The compound tag does't contain a itemStack")

fun ItemStack.toByteArray(): ByteArray {
    val nbt = serializeToNBT()

    return nbt.toByteArray()
}

fun itemFromByteArray(bytes: ByteArray): ItemStack {
    return itemFromCompound(byteArrayToTag(bytes) as CompoundTag)
}

fun ItemStack.toBase64(): String = Base64Coder.encodeLines(toByteArray())

fun itemFromBase64(data: String): ItemStack = itemFromByteArray(Base64Coder.decodeLines(data))