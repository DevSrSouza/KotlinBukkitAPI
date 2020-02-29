package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate.localcache

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.Reference
import kotlin.reflect.KProperty

class ExposedReferenceReadWrite<REF:Comparable<REF>, RID:Comparable<RID>, T: Entity<RID>, ID : Comparable<ID>>(
        val reference: Reference<REF, RID, T>,
        override val entity: Entity<ID>
) : ReadWriteExposedProperty<T, ID> {

    override fun get(desc: KProperty<*>): T {
        return entity.run { reference.getValue(this, desc) }
    }

    override fun set(value: T?, desc: KProperty<*>) {
        if(value != null)
            entity.apply { reference.setValue(this, desc, value) }
    }
}