package br.com.devsrsouza.kotlinbukkitapi.utility.collections

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.registerEvents
import br.com.devsrsouza.kotlinbukkitapi.extensions.unregisterListener
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

public typealias WhenPlayerQuitCollectionCallback = Player.() -> Unit
public typealias WhenPlayerQuitMapCallback<V> = Player.(V) -> Unit

// List

public fun Plugin.onlinePlayerListOf(): OnlinePlayerList = OnlinePlayerList(this)

public fun WithPlugin<*>.onlinePlayerListOf(): OnlinePlayerList = plugin.onlinePlayerListOf()

public fun onlinePlayerListOf(vararg players: Player, plugin: Plugin): OnlinePlayerList = OnlinePlayerList(plugin).apply { addAll(players) }

public fun Plugin.onlinePlayerListOf(vararg players: Player): OnlinePlayerList = onlinePlayerListOf(*players, plugin = this)

public fun WithPlugin<*>.onlinePlayerListOf(vararg players: Player): OnlinePlayerList = plugin.onlinePlayerListOf(*players)

public fun onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: Plugin): OnlinePlayerList = OnlinePlayerList(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

public fun Plugin.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>): OnlinePlayerList = onlinePlayerListOf(*pair, plugin = this)

public fun WithPlugin<*>.onlinePlayerListOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>): OnlinePlayerList = plugin.onlinePlayerListOf(*pair)

// Set

public fun Plugin.onlinePlayerSetOf(): OnlinePlayerSet = OnlinePlayerSet(this)

public fun WithPlugin<*>.onlinePlayerSetOf(): OnlinePlayerSet = plugin.onlinePlayerSetOf()

public fun onlinePlayerSetOf(vararg players: Player, plugin: Plugin): OnlinePlayerSet = OnlinePlayerSet(plugin).apply { addAll(players) }

public fun Plugin.onlinePlayerSetOf(vararg players: Player): OnlinePlayerSet = onlinePlayerSetOf(*players, plugin = this)

public fun WithPlugin<*>.onlinePlayerSetOf(vararg players: Player): OnlinePlayerSet = plugin.onlinePlayerSetOf(*players)

public fun onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>, plugin: Plugin): OnlinePlayerSet = OnlinePlayerSet(plugin).apply { pair.forEach { (player, whenPlayerQuit) -> add(player, whenPlayerQuit) } }

public fun Plugin.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>): OnlinePlayerSet = onlinePlayerSetOf(*pair, plugin = this)

public fun WithPlugin<*>.onlinePlayerSetOf(vararg pair: Pair<Player, WhenPlayerQuitCollectionCallback>): OnlinePlayerSet = plugin.onlinePlayerSetOf(*pair)

// Map

public fun <V> Plugin.onlinePlayerMapOf(): OnlinePlayerMap<V> = OnlinePlayerMap<V>(this)

public fun <V> WithPlugin<*>.onlinePlayerMapOf(): OnlinePlayerMap<V> = plugin.onlinePlayerMapOf<V>()

public fun <V> onlinePlayerMapOf(vararg pair: Pair<Player, V>, plugin: Plugin): OnlinePlayerMap<V> = OnlinePlayerMap<V>(plugin).apply { putAll(pair) }

public fun <V> Plugin.onlinePlayerMapOf(vararg pair: Pair<Player, V>): OnlinePlayerMap<V> = onlinePlayerMapOf(*pair, plugin = this)

public fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg pair: Pair<Player, V>): OnlinePlayerMap<V> = plugin.onlinePlayerMapOf(*pair)

public fun <V> onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>, plugin: Plugin): OnlinePlayerMap<V> = OnlinePlayerMap<V>(plugin).apply { triple.forEach { (player, value, whenPlayerQuit) -> put(player, value, whenPlayerQuit) } }

public fun <V> Plugin.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>): OnlinePlayerMap<V> = onlinePlayerMapOf(*triple, plugin = this)

public fun <V> WithPlugin<*>.onlinePlayerMapOf(vararg triple: Triple<Player, V, WhenPlayerQuitMapCallback<V>>): OnlinePlayerMap<V> = plugin.onlinePlayerMapOf(*triple)

public class OnlinePlayerList(override val plugin: Plugin) : LinkedList<Player>(), OnlinePlayerCollection {
    private val whenQuit: MutableList<Pair<Player, WhenPlayerQuitCollectionCallback>> = LinkedList()

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
            checkRegistration()
        }
    }

    override fun removeLastOccurrence(p0: Any?): Boolean {
        return super.removeLastOccurrence(p0).also {
            if(it) checkRegistration()
        }
    }

    override fun removeAt(p0: Int): Player {
        return super.removeAt(p0).also {
            checkRegistration()
        }
    }

    override fun remove(element: Player): Boolean {
        if(super.remove(element)) {
            checkRegistration()
            return true
        } else return false
    }

    override fun removeLast(): Player {
        return super.removeLast().also {
            checkRegistration()
        }
    }
}

public class OnlinePlayerSet(override val plugin: Plugin) : HashSet<Player>(), OnlinePlayerCollection {
    private val whenQuit: MutableMap<Player, WhenPlayerQuitCollectionCallback> = mutableMapOf()

    override fun add(player: Player, whenPlayerQuitCallback: WhenPlayerQuitCollectionCallback): Boolean {
        if(super<OnlinePlayerCollection>.add(player, whenPlayerQuitCallback)) {
            whenQuit.put(player, whenPlayerQuitCallback)

            checkRegistration()
            return true
        } else return false
    }

    override fun add(element: Player): Boolean {
        return super<HashSet>.add(element).also {
            if(it) checkRegistration()
        }
    }

    override fun remove(element: Player): Boolean {
        return super.remove(element).also {
            if(it) checkRegistration()
        }
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

public interface OnlinePlayerCollection : MutableCollection<Player>, KListener<Plugin> {


    /**
     * Adds a new Player to the collection with a callback for when the player quits the server.
     */
    public fun add(player: Player, whenPlayerQuit: WhenPlayerQuitCollectionCallback): Boolean {
        return add(player).also {
            if(it) checkRegistration()
        }
    }

    /**
     * Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback] provided.
     */
    public fun quit(player: Player): Boolean {
        return remove(player).also {
            if(it) checkRegistration()
        }
    }

    /**
     * Clear the collection calling all [WhenPlayerQuitCollectionCallback] from the Players.
     */
    public fun clearQuiting() {
        toMutableList().forEach {
            quit(it)
        }
    }
}

internal fun OnlinePlayerCollection.checkRegistration() {
    if(size == 1) {
        event<PlayerQuitEvent> { quit(player) }
        event<PlayerKickEvent> { quit(player) }
    } else if(size == 0) {
        unregisterListener()
    }
}

public class OnlinePlayerMap<V>(override val plugin: Plugin) : HashMap<Player, V>(), KListener<Plugin> {
    private val whenQuit: HashMap<Player, WhenPlayerQuitMapCallback<V>> = hashMapOf()

    /**
     * Puts a Player to the map with a [value] and a callback for when the player quits the server.
     */
    public fun put(key: Player, value: V, whenPlayerQuit: WhenPlayerQuitMapCallback<V>): V? {
        whenQuit.put(key, whenPlayerQuit)
        return put(key, value).also {
            checkRegistration()
        }
    }

    /**
     * Removes the player from the map, calling the [WhenPlayerQuitMapCallback] provided.
     */
    public fun quit(player: Player) {
        remove(player)?.also {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player, it)
            }
            checkRegistration()
        }
    }

    /**
     * Clear the map calling all [WhenPlayerQuitMapCallback] from the Players.
     */
    public fun clearQuiting() {
        keys.toMutableList().forEach {
            quit(it)
        }
    }

    override fun remove(key: Player): V? {
        return super.remove(key).also {
            checkRegistration()
        }
    }

    override fun remove(key: Player, value: V): Boolean {
        return super.remove(key, value).also {
            checkRegistration()
        }
    }

    private fun checkRegistration() {
        if(size == 1) {
            event<PlayerQuitEvent> { quit(player) }
            event<PlayerKickEvent> { quit(player) }
        } else if(size == 0) {
            unregisterListener()
        }
    }
}