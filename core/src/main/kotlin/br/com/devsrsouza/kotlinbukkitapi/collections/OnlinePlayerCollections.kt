package br.com.devsrsouza.kotlinbukkitapi.collections

import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.registerEvents
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.unregisterAll
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

typealias WhenPlayerQuitCollectionCallback = Player.() -> Unit
typealias WhenPlayerQuitMapCallback<V> = Player.(V) -> Unit

fun Plugin.onlinePlayerListOf() = OnlinePlayerList(this)

fun WithPlugin<*>.onlinePlayerListOf() = plugin.onlinePlayerListOf()

fun onlinePlayerListOf(vararg players: Player, plugin: Plugin)
        = OnlinePlayerList(plugin).apply { addAll(players) }

fun Plugin.onlinePlayerListOf(vararg players: Player)
        = onlinePlayerListOf(*players, plugin = this)

fun WithPlugin<*>.onlinePlayerListOf(vararg players: Player)
        = plugin.onlinePlayerListOf(*players)

fun onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: Plugin)
        = OnlinePlayerList(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun Plugin.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = onlinePlayerListOf(*pair, plugin = this)

fun WithPlugin<*>.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = plugin.onlinePlayerListOf(*pair)

fun Plugin.onlinePlayerSetOf() = OnlinePlayerSet(this)

fun WithPlugin<*>.onlinePlayerSetOf() = plugin.onlinePlayerSetOf()

fun onlinePlayerSetOf(vararg players: Player, plugin: Plugin)
        = OnlinePlayerSet(plugin).apply { addAll(players) }

fun Plugin.onlinePlayerSetOf(vararg players: Player)
        = onlinePlayerSetOf(*players, plugin = this)

fun WithPlugin<*>.onlinePlayerSetOf(vararg players: Player)
        = plugin.onlinePlayerSetOf(*players)

fun onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: Plugin)
        = OnlinePlayerSet(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

fun Plugin.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = onlinePlayerSetOf(*pair, plugin = this)

fun WithPlugin<*>.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>)
        = plugin.onlinePlayerSetOf(*pair)

fun <V> Plugin.onlinePlayerMapOf() = OnlinePlayerMap<V>(this)

fun <V> WithPlugin<*>.onlinePlayerMapOf() = plugin.onlinePlayerMapOf<V>()

fun <V> onlinePlayerMapOf(vararg pair: Pair<Player, V>, plugin: Plugin)
        = OnlinePlayerMap<V>(plugin).apply { putAll(pair) }

fun <V> Plugin.onlinePlayerMapOf(vararg pair: Pair<Player, V>)
        = onlinePlayerMapOf(*pair, plugin = this)

fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg pair: Pair<Player, V>)
        = plugin.onlinePlayerMapOf(*pair)

fun <V> onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>, plugin: Plugin)
        = OnlinePlayerMap<V>(plugin).apply { triple.forEach { (player, value, whenPlayerQuit) -> put(player, value, whenPlayerQuit) } }

fun <V> Plugin.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>)
        = onlinePlayerMapOf(*triple, plugin = this)

fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>)
        = plugin.onlinePlayerMapOf(*triple)

class OnlinePlayerList(override val plugin: Plugin) : LinkedList<Player>(), OnlinePlayerCollection {
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

class OnlinePlayerSet(override val plugin: Plugin) : LinkedHashSet<Player>(), OnlinePlayerCollection {
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

interface OnlinePlayerCollection : MutableCollection<Player>, KListener<Plugin> {

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

    fun clearQuiting() {
        toMutableList().forEach {
            quit(it)
        }
    }
}

class OnlinePlayerMap<V>(override val plugin: Plugin) : LinkedHashMap<Player, V>(), KListener<Plugin> {
    private val whenQuit: MutableMap<Player, WhenPlayerQuitMapCallback<V>> = mutableMapOf()

    init {
        event<PlayerQuitEvent> { quit(player) }
        event<PlayerKickEvent> { quit(player) }
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

    fun clearQuiting() {
        keys.toMutableList().forEach {
            quit(it)
        }
    }
}