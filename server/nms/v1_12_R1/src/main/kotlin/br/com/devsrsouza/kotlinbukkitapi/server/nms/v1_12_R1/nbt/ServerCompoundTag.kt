package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_12_R1.nbt

import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_12_R1.nbt.ServerNBTImpl.getMap
import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.tags.CompoundTag
import net.minecraft.server.v1_12_R1.NBTTagCompound

class ServerCompoundTag(val nmsCompound: NBTTagCompound) : CompoundTag() {

    init {
        for ((key, tag) in nmsCompound.getMap())
            super.put(key, ServerNBTImpl.serverTagAsKotlinNBTTag(tag))
    }

    override fun put(key: String, value: ITag): ITag? {
        nmsCompound.set(key, ServerNBTImpl.kotlinNBTTagAsServerTag(value))
        return super.put(key, value)
    }

    override fun putAll(from: Map<out String, ITag>) {
        for ((key, tag) in from)
            nmsCompound.set(key, ServerNBTImpl.kotlinNBTTagAsServerTag(tag))

        super.putAll(from)
    }

    override fun remove(key: String): ITag? {
        nmsCompound.remove(key)
        return super.remove(key)
    }

}
