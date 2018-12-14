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

class OnlinePlayerList(private val plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedList<Player>(), Listener {
    private val whenQuit: MutableMap<Player, Player.() -> Unit> = mutableMapOf()

    init {
        event<PlayerQuitEvent> { quit(player) }
        event<PlayerKickEvent> { quit(player) }
    }

    fun add(player: Player, whenPlayerQuit: Player.() -> Unit) : Boolean {
        whenQuit.put(player, whenPlayerQuit)
        if(isEmpty()) registerEvents(plugin)
        return add(player)
    }

    fun quit(player: Player) {
        if(remove(player)) {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player)
            }
            if(isEmpty()) unregisterAll()
        }
    }

    override fun removeFirst(): Player {
        if (isEmpty()) unregisterAll()
        return super.removeFirst()
    }

    override fun removeLastOccurrence(p0: Any?): Boolean {
        if (isEmpty()) unregisterAll()
        return super.removeLastOccurrence(p0)
    }

    override fun removeAt(p0: Int): Player {
        if (isEmpty()) unregisterAll()
        return super.removeAt(p0)
    }

    override fun remove(element: Player): Boolean {
        if (isEmpty()) unregisterAll()
        return super.remove(element)
    }

    override fun removeLast(): Player {
        if (isEmpty()) unregisterAll()
        return super.removeLast()
    }
}

class OnlinePlayerMap<V>(private val plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedHashMap<Player, V>(), Listener {
    private val whenQuit: MutableMap<Player, Player.(V) -> Unit> = mutableMapOf()

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
        if(isEmpty()) unregisterAll()
        return super.remove(key)
    }

    override fun remove(key: Player, value: V): Boolean {
        if(isEmpty()) unregisterAll()
        return super.remove(key, value)
    }
}