package br.com.devsrsouza.kotlinbukkitapi.collections

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler.scheduler
import br.com.devsrsouza.kotlinbukkitapi.utils.time.now
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

typealias OnExpireMapCallback<K, V> = (K, V) -> Unit

fun <K, V> Plugin.expirationMapOf(): ExpirationMap<K, V> = ExpirationMapImpl(this)

fun <K, V> WithPlugin<*>.expirationMapOf() = plugin.expirationMapOf<K, V>()

fun <K, V> expirationMapOf(expireTime: Long, plugin: Plugin, vararg elements: Pair<K, V>)
        = plugin.expirationMapOf<K, V>().apply { elements.forEach { (key, value) -> put(key, value, expireTime) } }

fun <K, V> Plugin.expirationMapOf(expireTime: Long, vararg elements: Pair<K, V>)
        = expirationMapOf(expireTime, this, *elements)

fun <K, V> WithPlugin<*>.expirationMapOf(expireTime: Long, vararg elements: Pair<K, V>)
        = plugin.expirationMapOf(expireTime, *elements)

fun <K, V> expirationMapOf(expireTime: Long, plugin: Plugin, vararg elements: Triple<K, V, OnExpireMapCallback<K, V>>)
        = plugin.expirationMapOf<K, V>().apply { elements.forEach { (key, value, onExpire) -> put(key, value, expireTime, onExpire) } }

fun <K, V> Plugin.expirationMapOf(expireTime: Long, vararg elements: Triple<K, V, OnExpireMapCallback<K, V>>)
        = expirationMapOf(expireTime, this, *elements)

fun <K, V> WithPlugin<*>.expirationMapOf(expireTime: Long, vararg elements: Triple<K, V, OnExpireMapCallback<K, V>>)
        = plugin.expirationMapOf(expireTime, *elements)

interface ExpirationMap<K, V> : MutableMap<K, V>, WithPlugin<Plugin> {
    fun missingTime(key: K): Long?
    fun expire(key: K, time: Long): Boolean
    fun expire(key: K, time: Long, callback: OnExpireMapCallback<K, V>): Boolean

    fun put(key: K, value: V, time: Long): V?
    fun put(key: K, value: V, time: Long, callback: OnExpireMapCallback<K, V>): V?
}

class ExpirationMapImpl<K, V>(
        override val plugin: Plugin,
        val initialMap: MutableMap<K ,V> = mutableMapOf()
) : ExpirationMap<K, V>, MutableMap<K, V> by initialMap {

    private val putTime: MutableMap<K, Long> = mutableMapOf()
    private val expiration: MutableMap<K, Long> = mutableMapOf()
    private val whenExpire: MutableMap<K, OnExpireMapCallback<K, V>> = mutableMapOf()

    override fun missingTime(key: K): Long? = if (containsKey(key))
        expiration.getOrDefault(key, 0) - ((now() - putTime.getOrPut(key) { now() }) / 1000)
    else null

    private fun ex(key: K, time: Long) {
        putTime.put(key, now())
        expiration.put(key, time)
    }

    private fun whenEx(key: K, callback: OnExpireMapCallback<K, V>) {
        whenExpire.put(key, callback)
    }

    override fun expire(key: K, time: Long): Boolean {
        if(containsKey(key)) {
            ex(key, time)
            return true
        } else return false
    }

    override fun expire(key: K, time: Long, callback: OnExpireMapCallback<K, V>): Boolean {
        if(expire(key, time)) {
            whenEx(key, callback)
            return true
        } else return false
    }

    override fun put(key: K, value: V): V? {
        val result = initialMap.put(key, value)
        generateTask()
        return result
    }

    override fun put(key: K, value: V, time: Long): V? {
        if(time <= 0) throw IllegalArgumentException("expiration time can't be negative or zero")
        val result = put(key, value)
        ex(key, time)
        return result
    }

    override fun put(key: K, value: V, time: Long, callback: OnExpireMapCallback<K, V>): V? {
        if(time <= 0) throw IllegalArgumentException("expiration time can't be negative or zero")
        val result = put(key, value)
        ex(key, time)
        whenEx(key, callback)
        return result
    }

    override fun remove(key: K): V? {
        val result = initialMap.remove(key)
        if(result != null) {
            putTime.remove(key)
            expiration.remove(key)
            whenExpire.remove(key)
        }
        return result
    }

    private fun checkTime(current: Long, key: K)
            = ((current - (putTime.getOrPut(key) { current })) / 1000) - (expiration.get(key) ?: 0) >= 0

    private var task: BukkitTask? = null
    private var emptyCount: Byte = 0
    private fun generateTask() {
        if (task == null) {
            task = scheduler {
                if (isEmpty())
                    emptyCount++
                else {
                    emptyCount = 0
                    val current = now()
                    for ((key, value) in entries) {
                        if (checkTime(current, key)) {
                            whenExpire.remove(key)?.invoke(key, value)
                            initialMap.remove(key)
                            putTime.remove(key)
                            expiration.remove(key)
                        }
                    }
                }
                if (emptyCount > 5) {
                    cancel()
                    task = null
                }
            }.runTaskTimer(plugin, 0L, 20L)
        }
    }
}