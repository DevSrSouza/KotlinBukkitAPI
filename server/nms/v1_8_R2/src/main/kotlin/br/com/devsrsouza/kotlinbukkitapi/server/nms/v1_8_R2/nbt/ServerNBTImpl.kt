package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R2.nbt

import br.com.devsrsouza.kotlinbukkitapi.server.api.nbt.ServerNBT
import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.TagType
import br.com.devsrsouza.kotlinnbt.api.tags.*
import net.minecraft.server.v1_8_R2.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

object ServerNBTImpl : ServerNBT<NBTTagCompound, NBTBase> {
    val mapField by lazy { NBTTagCompound::class.java.getDeclaredField("map") }
    
    fun NBTTagCompound.getMap(): Map<String, NBTBase> {
        return mapField.get(this) as Map<String, NBTBase>
    }

    override fun serverTagAsKotlinNBTTag(tag: NBTBase): ITag {
        return when(tag) {
            is NBTTagString -> StringTag(tag.a_())
            is NBTTagDouble -> DoubleTag(tag.g())
            is NBTTagFloat -> FloatTag(tag.h())
            is NBTTagLong -> LongTag(tag.c())
            is NBTTagInt -> IntTag(tag.d())
            is NBTTagShort -> ShortTag(tag.e())
            is NBTTagByte -> ByteTag(tag.f())
            is NBTTagByteArray -> ByteArrayTag(tag.c())
            is NBTTagIntArray -> IntArrayTag(tag.c())
            is NBTTagCompound -> compoundAsKotlinNBT(tag)
            is NBTTagList -> {
                val typeId = if(tag.isEmpty) TagType.END else TagType.byID(tag.g(0).getTypeId())!!

                return ServerListTag(typeId.clazz as KClass<ITag>, tag)
            }
            else -> throw IllegalArgumentException("NBT tag not support on KotlinNBT")
        }
    }

    override fun kotlinNBTTagAsServerTag(tag: ITag): NBTBase {
        return when(tag) {
            is StringTag -> NBTTagString(tag.value)
            is DoubleTag -> NBTTagDouble(tag.value)
            is FloatTag -> NBTTagFloat(tag.value)
            is LongTag -> NBTTagLong(tag.value)
            is IntTag -> NBTTagInt(tag.value)
            is ShortTag -> NBTTagShort(tag.value)
            is ByteTag -> NBTTagByte(tag.value)
            is ByteArrayTag -> NBTTagByteArray(tag.value)
            is IntArrayTag -> NBTTagIntArray(tag.value)
            is CompoundTag -> kotlinNBTAsCompound(tag)
            is ListTag<*> -> NBTTagList().also { list ->
                tag.forEach { list.add(kotlinNBTTagAsServerTag(it)) }
            }
            else -> throw IllegalArgumentException("tag ${tag.type} is not supported in this version.")
        }
    }

    override fun compoundAsKotlinNBT(nbt: NBTTagCompound): CompoundTag {
        return ServerCompoundTag(nbt)
    }

    override fun kotlinNBTAsCompound(compound: CompoundTag): NBTTagCompound {
        val nbt = NBTTagCompound()

        for (tag in compound) {
            runCatching { kotlinNBTTagAsServerTag(tag.value) }.getOrNull()?.also {
                nbt.set(tag.key, it)
            }
        }

        return nbt
    }
}