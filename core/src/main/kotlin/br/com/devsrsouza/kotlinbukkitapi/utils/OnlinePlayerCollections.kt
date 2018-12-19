package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.registerEvents
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.unregisterAll
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

typealias WhenPlayerQuitCollectionCallback = Player.() -> Unit
typealias WhenPlayerQuitMapCallback<V> = Player.(V) -> Unit

fun onlinePlayerListOf(plugin: Plugin = KotlinBukkitAPI.INSTANCE) = OnlinePlayerList(plugin)

fun onlinePlayerListOf(vararg players: Player, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = OnlinePlayerList(plugin).apply { addAll(players) }

fun onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = OnlinePlayerList(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun onlinePlayerSetOf(plugin: Plugin = KotlinBukkitAPI.INSTANCE) = OnlinePlayerSet(plugin)

fun onlinePlayerSetOf(vararg players: Player, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = OnlinePlayerSet(plugin).apply { addAll(players) }

fun onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = OnlinePlayerSet(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun <V> onlinePlayerMapOf(plugin: Plugin = KotlinBukkitAPI.INSTANCE) = OnlinePlayerMap<V>(plugin)

fun <V> onlinePlayerMapOf(vararg pair: Pair<Player, V>, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = OnlinePlayerMap<V>(plugin).apply { putAll(pair) }

fun <V> onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>, plugin: Plugin = KotlinBukkitAPI.INSTANCE)
        = OnlinePlayerMap<V>(plugin).apply { triple.forEach { (player, value, whenPlayerQuit) -> put(player, value, whenPlayerQuit) } }

class OnlinePlayerList(override val plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedList<Player>(), OnlinePlayerCollection {
    private val whenQuit: MutableList<Pair<Player, WhenPlayerQuitCollectionCallback>> = LinkedList()

    init { init() }

    override fun add(player: Player, whenPlayerQuitCallback: Player.() -> Unit): Boolean {
        if(super<OnlinePlayerCollection>.add(player, whenPlayerQuitCallback)) {
            whenQuit.add(player to whenPlayerQuitCallback)
            return true
        } else return false
    }

    override fun add(element: Player): Boolean {
        if (isEmpty()) registerEvents(plugin)
        return super<LinkedList>.add(element)
    }

    override fun quit(player: Player): Boolean {
        if(super.quit(player)) {
            val iterator = whenQuit.iterator()
            for (pair in iterator) {
                if(pair.first == player) {
                    iterator.remove()
                    pair.second.invoke(pair.first)
                }
            }
            return true
        } else return false
    }

    override fun removeFirst(): Player {
        return super.removeFirst().also {
            if (isEmpty()) unregisterAll()
        }
    }

    override fun removeLastOccurrence(p0: Any?): Boolean {
        return super.removeLastOccurrence(p0).also {
            if (isEmpty()) unregisterAll()
        }
    }

    override fun removeAt(p0: Int): Player {
        return super.removeAt(p0).also {
            if (isEmpty()) unregisterAll()
        }
    }

    override fun remove(element: Player): Boolean {
        if(super.remove(element)) {
            if (isEmpty()) unregisterAll()
            return true
        } else return false
    }

    override fun removeLast(): Player {
        return super.removeLast().also {
            if (isEmpty()) unregisterAll()
        }
    }
}

class OnlinePlayerSet(override val plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedHashSet<Player>(), OnlinePlayerCollection {
    private val whenQuit: MutableMap<Player, WhenPlayerQuitCollectionCallback> = mutableMapOf()

    init { init() }

    override fun add(player: Player, whenPlayerQuitCallback: Player.() -> Unit): Boolean {
        if(super<OnlinePlayerCollection>.add(player, whenPlayerQuitCallback)) {
            whenQuit.put(player, whenPlayerQuitCallback)
            return true
        } else return false
    }

    override fun add(element: Player): Boolean {
        val empty = isEmpty()
        val added = super<LinkedHashSet>.add(element)

        if(added && empty) registerEvents(plugin)

        return added
    }

    override fun remove(element: Player): Boolean {
        if(super.remove(element)) {
            if (isEmpty()) unregisterAll()
            return true
        } else return false
    }

    override fun quit(player: Player): Boolean {
        if(super.quit(player)) {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player)
            }
            return true
        } else return false
    }
}

interface OnlinePlayerCollection : MutableCollection<Player>, Listener {

    val plugin: Plugin

    fun init() {
        event<PlayerQuitEvent> { quit(player) }
        event<PlayerKickEvent> { quit(player) }
    }

    fun add(player: Player, whenPlayerQuit: Player.() -> Unit): Boolean {
        if (isEmpty()) registerEvents(plugin)
        return add(player)
    }

    fun quit(player: Player): Boolean {
        if (remove(player)) {
            if (isEmpty()) unregisterAll()
            return true
        } else return false
    }
}

class OnlinePlayerMap<V>(private val plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedHashMap<Player, V>(), Listener {
    private val whenQuit: MutableMap<Player, WhenPlayerQuitMapCallback<V>> = mutableMapOf()

    init {
        event<PlayerQuitEvent> { quit(player) }
        event<PlayerKickEvent> { quit(player) }

        registerEvents(plugin)
    }

    fun put(key: Player, value: V, whenPlayerQuit: Player.(V) -> Unit): V? {
        whenQuit.put(key, whenPlayerQuit)
        if(isEmpty()) registerEvents(plugin)
        return put(key, value)
    }

    fun quit(player: Player) {
        remove(player)?.also {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player, it)
            }
            if(isEmpty()) unregisterAll()
        }
    }

    override fun remove(key: Player): V? {
        return super.remove(key).also {
            if(isEmpty()) unregisterAll()
        }
    }

    override fun remove(key: Player, value: V): Boolean {
        return super.remove(key, value).also {
            if(isEmpty()) unregisterAll()
        }
    }
}