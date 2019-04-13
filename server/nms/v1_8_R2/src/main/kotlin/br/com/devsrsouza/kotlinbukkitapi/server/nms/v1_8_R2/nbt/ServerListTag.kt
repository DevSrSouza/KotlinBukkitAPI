package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R2.nbt

import br.com.devsrsouza.kotlinnbt.api.ITag
import br.com.devsrsouza.kotlinnbt.api.tags.ListTag
import net.minecraft.server.v1_8_R2.NBTBase
import net.minecraft.server.v1_8_R2.NBTTagList
import kotlin.reflect.KClass

class ServerListTag(clazz: KClass<ITag>, val nmsList: NBTTagList) : ListTag<ITag>(clazz) {

    init {
        for(i in 0 until nmsList.size()) {
            val baseTag = nmsList.g(i)
            super.add(ServerNBTImpl.serverTagAsKotlinNBTTag(baseTag))
        }
    }

    override fun add(element: ITag): Boolean {
        nmsList.add(ServerNBTImpl.kotlinNBTTagAsServerTag(element))
        return super.add(element)
    }

    override fun add(index: Int, element: ITag) {
        nmsList.add(ServerNBTImpl.kotlinNBTTagAsServerTag(element))
        super.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<ITag>): Boolean {
        for (element in elements) {
            nmsList.add(ServerNBTImpl.kotlinNBTTagAsServerTag(element))
        }
        return super.addAll(index, elements)
    }

    override fun addAll(elements: Collection<ITag>): Boolean {
        for (element in elements) {
            nmsList.add(ServerNBTImpl.kotlinNBTTagAsServerTag(element))
        }
        return super.addAll(elements)
    }

    fun removeNms(element: NBTBase) {
        for(i in 0 until nmsList.size()) {
            if(nmsList.g(i).equals(element)) {
                nmsList.a(i)
                break
            }
        }
    }

    override fun remove(element: ITag): Boolean {
        val nmsElement = ServerNBTImpl.kotlinNBTTagAsServerTag(element)
        removeNms(nmsElement)
        return super.remove(element)
    }

    override fun removeAll(elements: Collection<ITag>): Boolean {
        for (element in elements) {
            val nmsElement = ServerNBTImpl.kotlinNBTTagAsServerTag(element)
            removeNms(nmsElement)
        }
        return super.removeAll(elements)
    }

    override fun removeAt(index: Int): ITag {
        nmsList.a(index)
        return super.removeAt(index)
    }

    override fun set(index: Int, element: ITag): ITag {
        nmsList.a(index, ServerNBTImpl.kotlinNBTTagAsServerTag(element))
        return super.set(index, element)
    }
}