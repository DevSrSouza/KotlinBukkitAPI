package br.com.devsrsouza.kotlinbukkitapi.exposed

import org.jetbrains.exposed.dao.Entity
import kotlin.reflect.KProperty

interface ExposedDelegate<T> {

    operator fun <ID : Comparable<ID>> getValue(
            entity: Entity<ID>,
            desc: KProperty<*>
    ): T

    operator fun <ID : Comparable<ID>> setValue(
            entity: Entity<ID>,
            desc: KProperty<*>,
            value: T
    )

}