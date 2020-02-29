package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate.localcache

import br.com.devsrsouza.kotlinbukkitapi.exposed.delegate.ExposedDelegate
import org.jetbrains.exposed.dao.Entity
import kotlin.reflect.KProperty

interface ReadWriteExposedProperty<T, ID : Comparable<ID>> {
    val entity: Entity<ID>

    fun get(desc: KProperty<*>): T
    fun set(value: T?, desc: KProperty<*>)
}


class ExposedLocalCacheDelegate<T>(
        private val readWrite: ReadWriteExposedProperty<T, *>
) : ExposedDelegate<T> {

    private var isCached: Boolean = false
    private var cache: T? = null

    override fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): T {
        if(cache != null) {
            loadCache(desc)
            isCached = true
        }

        return cache!!
    }

    override fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: T
    ) {
        cache = value
    }

    fun loadCache(property: KProperty<*>) {
        cache = readWrite.get(property)
    }

    fun invalidateCache() {
        cache = null
        isCached = false
    }

    fun flushCache(property: KProperty<*>) {
        if(isCached)
            readWrite.set(cache, property)
    }
}
