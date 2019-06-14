package br.com.devsrsouza.kotlinbukkitapi.exposed

import org.jetbrains.exposed.dao.Entity
import kotlin.reflect.KProperty

fun <T> ExposedDelegate<T>.cached() = CachedExposedDelegate(this)

class CachedExposedDelegate<T>(
        private val delegate: ExposedDelegate<T>
) : ExposedDelegate<T> {
    private var cache: T? = null
    private var isCached: Boolean = false

    override fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): T {
        if (!isCached) {
            cache = delegate.getValue(entity, desc)
            isCached = true
        }

        return cache!!
    }

    override fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: T
    ) {
        delegate.setValue(entity, desc, value)
        cache = value
        isCached = true
    }
}