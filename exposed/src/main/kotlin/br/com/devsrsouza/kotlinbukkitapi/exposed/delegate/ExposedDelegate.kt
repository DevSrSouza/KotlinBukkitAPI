package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate

import org.jetbrains.exposed.dao.Entity
import kotlin.reflect.KProperty

public interface ExposedDelegate<T> {

    public operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): T

    public operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: T
    )

}