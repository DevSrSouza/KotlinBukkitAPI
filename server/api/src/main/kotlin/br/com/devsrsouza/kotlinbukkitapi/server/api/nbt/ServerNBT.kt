package br.com.devsrsouza.kotlinbukkitapi.server.api.nbt

import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.tags.CompoundTag

interface ServerNBT<ServerCompound, ServerBaseTag> {

    fun serverTagAsKotlinNBTTag(tag: ServerBaseTag): ITag

    fun kotlinNBTTagAsServerTag(tag: ITag): ServerBaseTag

    fun compoundAsKotlinNBT(nbt: ServerCompound): CompoundTag

    fun kotlinNBTAsCompound(compound: CompoundTag): ServerCompound
}