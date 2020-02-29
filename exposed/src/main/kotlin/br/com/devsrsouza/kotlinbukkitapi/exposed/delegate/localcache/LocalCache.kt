package br.com.devsrsouza.kotlinbukkitapi.exposed.delegate.localcache

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

/**
 * Caches a reference of Entity for you get and change outside a transaction block.
 * usage: `val address by localCache(Address referencedOn UserTable.address)`
 *
 * ```
 * transaction {
 *   val user = UserDao.findById(theId)
 *
 *   user.loadLocalCache()
 *   user.flushLocalCache()
 * }
 * ```
 */

fun <REF : Comparable<REF>, RID : Comparable<RID>, T : Entity<RID>, ID : Comparable<ID>>
        Entity<ID>.localCache(
        optionalReference: OptionalReference<REF, RID, T>
) = ExposedLocalCacheDelegate(ExposedOptionalReferenceReadWrite(optionalReference, this))

fun <REF : Comparable<REF>, RID : Comparable<RID>, T : Entity<RID>, ID : Comparable<ID>>
        Entity<ID>.localCache(
        reference: Reference<REF, RID, T>
) = ExposedLocalCacheDelegate(ExposedReferenceReadWrite(reference, this))

/**
 * loads the cache for delegate properties [localCache].
 *
 * Require be used inside a transaction.
 */
fun Entity<*>.loadLocalCache() {
    TransactionManager.current() // check if is in a transaction context

    runExposedLocalCacheDelegate { property, delegate ->
        delegate.loadCache(property)
    }
}

/**
 * Flush the changes to the reference in [localCache].
 *
 * Require be used inside a transaction.
 */
fun Entity<*>.flushLocalCache() {
    TransactionManager.current() // check if is in a transaction context

    runExposedLocalCacheDelegate { property, delegate ->
        delegate.flushCache(property)
    }
}

private fun Entity<*>.runExposedLocalCacheDelegate(
        run: (KProperty1<*,*>, ExposedLocalCacheDelegate<*>) -> Unit
) {
    val properties = this::class.declaredMemberProperties
            .map { it as KProperty1<Any, Any> }
            .associateWith { it.getDelegate(this) as? ExposedLocalCacheDelegate<*> }

    for ((property, delegate) in properties) {
        if(delegate != null)
            run(property, delegate)
    }
}