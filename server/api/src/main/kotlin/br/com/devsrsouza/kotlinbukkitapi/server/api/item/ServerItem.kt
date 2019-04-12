package br.com.devsrsouza.kotlinbukkitapi.server.api.item

import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.tags.CompoundTag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

interface ServerItem {
    fun getTag(item: ItemStack) : CompoundTag

    fun setTag(item: ItemStack, compound: CompoundTag) : ItemStack

    fun addTag(item: ItemStack, key: String, value: ITag): ItemStack

    fun removeTag(item: ItemStack, key: String): ItemStack

    fun setHeadFromJson(skull: SkullMeta, json: String)

    fun setHeadFromUrl(skull: SkullMeta, url: String) {
        val json = "{\"textures\":{\"SKIN\":{\"url\":\"$url\"}}}"
        setHeadFromJson(skull, json)
    }
}