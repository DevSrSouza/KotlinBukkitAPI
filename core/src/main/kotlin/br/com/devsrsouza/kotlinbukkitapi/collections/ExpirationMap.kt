package br.com.devsrsouza.kotlinbukkitapi.collections

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler.scheduler
import br.com.devsrsouza.kotlinbukkitapi.utils.time.now
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

typealias OnExipereMapCallback<K, V> = (K, V) -> Unit

interface IExpirationMap<K, V> : MutableMap<K, V>, WithPlugin<Plugin> {
    fun missingTime(key: K): Long?
    fun expire(key: K, time: Long): Boolean
    fun expire(key: K, time: Long, callback: OnExipereMapCallback<K, V>): Boolean

    fun put(key: K, value: V, time: Long): V?
    fun put(key: K, value: V, time: Long, callback: OnExipereMapCallback<K, V>): V?
}

class ExpirationMap<K, V>(
        override val plugin: Plugin,
        val initialMap: MutableMap<K ,V> = mutableMapOf()
) : IExpirationMap<K, V>, MutableMap<K, V> by initialMap {

    private val putTime: MutableMap<K, Long> = mutableMapOf()
    private val expiration: MutableMap<K, Long> = mutableMapOf()
    private val whenExpire: MutableMap<K, OnExipereMapCallback<K, V>> = mutableMapOf()

    override fun missingTime(key: K): Long? = if (containsKey(key))
        expiration.getOrDefault(key, 0) - ((now() - putTime.getOrPut(key) { now() }) / 1000)
    else null

    private fun ex(key: K, time: Long) {
        putTime.put(key, now())
        expiration.put(key, time)
    }

    private fun whenEx(key: K, callback: OnExipereMapCallback<K, V>) {
        whenExpire.put(key, callback)
    }

    override fun expire(key: K, time: Long): Boolean {
        if(containsKey(key)) {
            ex(key, time)
            return true
        } else return false
    }

    override fun expire(key: K, time: Long, callback: OnExipereMapCallback<K, V>): Boolean {
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

    override fun put(key: K, value: V, time: Long, callback: OnExipereMapCallback<K, V>): V? {
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