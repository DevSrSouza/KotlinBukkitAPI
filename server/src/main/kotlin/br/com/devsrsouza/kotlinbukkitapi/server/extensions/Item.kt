package br.com.devsrsouza.kotlinbukkitapi.server.extensions

import br.com.devsrsouza.kotlinbukkitapi.server.Server
import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.tags.CompoundTag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

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