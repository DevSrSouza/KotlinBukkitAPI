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
        event<PlayerQuitEvent> { tryRemove(player) }
        event<PlayerKickEvent> { tryRemove(player) }
    }

    fun add(player: Player, whenPlayerQuit: Player.() -> Unit) : Boolean {
        whenQuit.put(player, whenPlayerQuit)
        if(isEmpty()) registerEvents(plugin)
        return add(player)
    }

    private fun tryRemove(player: Player) {
        if(remove(player)) {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player)
            }
            if(isEmpty()) unregisterAll()
        }
    }
}

class OnlinePlayerMap<V>(private val plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedHashMap<Player, V>(), Listener {
    private val whenQuit: MutableMap<Player, Player.(V) -> Unit> = mutableMapOf()

    init {
        event<PlayerQuitEvent> { tryRemove(player) }
        event<PlayerKickEvent> { tryRemove(player) }

        registerEvents(plugin)
    }

    fun put(key: Player, value: V, whenPlayerQuit: Player.(V) -> Unit): V? {
        whenQuit.put(key, whenPlayerQuit)
        if(isEmpty()) registerEvents(plugin)
        return put(key, value)
    }

    private fun tryRemove(player: Player) {
        remove(player)?.also {
            whenQuit.remove(player)?.also { block ->
                block.invoke(player, it)
            }
            if(isEmpty()) unregisterAll()
        }
    }
}