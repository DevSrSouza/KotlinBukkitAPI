package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.item

import br.com.devsrsouza.kotlinbukkitapi.server.api.item.ServerItem
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R3.nbt.ServerNBTImpl
import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.tags.CompoundTag
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_8_R3.NBTTagCompound
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSkull
import org.bukkit.inventory.ItemStack
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.util.*
import net.minecraft.server.v1_8_R3.ItemStack as NMSItemStack

object ServerItemImpl : ServerItem {
    val ItemStack.craftCopy: CraftItemStack get() = CraftItemStack.asCraftCopy(this)
    val CraftItemStack.nmsCopy: NMSItemStack get() = CraftItemStack.asNMSCopy(this)
    val NMSItemStack.craftMirror: CraftItemStack get() = CraftItemStack.asCraftMirror(this)

    val NMSItemStack.tagOrNewCompound: NBTTagCompound get() = tag ?: NBTTagCompound()

    val profileField by lazy { CraftSkull::class.java.getDeclaredField("profile") }

    override fun getTag(
            item: ItemStack
    ) = ServerNBTImpl.compoundAsKotlinNBT(item.craftCopy.nmsCopy.tagOrNewCompound)

    override fun setTag(
            item: ItemStack,
            compound: CompoundTag
    ): ItemStack = item.craftCopy.nmsCopy.apply {
        tag = ServerNBTImpl.kotlinNBTAsCompound(compound)
    }.craftMirror

    override fun addTag(
            item: ItemStack,
            key: String,
            value: ITag
    ): ItemStack = item.craftCopy.nmsCopy.apply {
        tagOrNewCompound.set(key, ServerNBTImpl.kotlinNBTTagAsServerTag(value))
    }.craftMirror

    override fun removeTag(
            item: ItemStack,
            key: String
    ): ItemStack = item.craftCopy.nmsCopy.apply {
        tagOrNewCompound.remove(key)
    }.craftMirror

    override fun setHeadFromJson(skull: SkullMeta, json: String) {
        val profile = GameProfile(UUID.randomUUID(), null).apply {
            properties.put("textures", Property("textures", Base64Coder.encodeString(json)))
        }
        profileField.set(skull, profile)
    }
}