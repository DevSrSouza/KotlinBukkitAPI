package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_13_R1.nbt

import br.com.devsrsouza.kotlinbukkitapi.server.api.nbt.ServerNBT
import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.TagType
import br.com.devsrsouza.kotlinnbt.api.tags.*
import net.minecraft.server.v1_13_R1.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

object ServerNBTImpl : ServerNBT<NBTTagCompound, NBTBase> {
    val mapField by lazy { NBTTagCompound::class.java.getDeclaredField("map") }
    
    fun NBTTagCompound.getMap(): Map<String, NBTBase> {
        return mapField.get(this) as Map<String, NBTBase>
    }

    override fun serverTagAsKotlinNBTTag(tag: NBTBase): ITag {
        return when(tag) {
            is NBTTagString -> StringTag(tag.b_())
            is NBTTagDouble -> DoubleTag(tag.asDouble())
            is NBTTagFloat -> FloatTag(tag.i())
            is NBTTagLong -> LongTag(tag.d())
            is NBTTagInt -> IntTag(tag.e())
            is NBTTagShort -> ShortTag(tag.f())
            is NBTTagByte -> ByteTag(tag.g())
            is NBTTagByteArray -> ByteArrayTag(tag.c())
            is NBTTagIntArray -> IntArrayTag(tag.d())
            is NBTTagCompound -> compoundAsKotlinNBT(tag)
            is NBTTagList -> {
                val typeId = if(tag.isEmpty()) TagType.END else TagType.byID(tag.get(0).getTypeId().toInt())!!

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