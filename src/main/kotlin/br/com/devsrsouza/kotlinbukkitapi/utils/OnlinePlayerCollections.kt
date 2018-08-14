package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.registerEvents
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.collections.LinkedHashMap

class OnlinePlayerList(plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedList<Player>(), Listener {
    private val bb: MutableMap<Player, Player.() -> Unit> = mutableMapOf()

    init {
        event<PlayerQuitEvent> { tryRemove(player) }
        event<PlayerKickEvent> { tryRemove(player) }

        registerEvents(plugin)
    }

    fun add(player: Player, whenPlayerQuit: Player.() -> Unit) : Boolean {
        bb.put(player, whenPlayerQuit)
        return add(player)
    }

    private fun tryRemove(player: Player) {
        if(remove(player)) {
            bb.remove(player)?.also { block ->
                block.invoke(player)
            }
        }
    }
}

class OnlinePlayerMap<V>(plugin: Plugin = KotlinBukkitAPI.INSTANCE) : LinkedHashMap<Player, V>(), Listener {
    private val bb: MutableMap<Player, Player.(V) -> Unit> = mutableMapOf()

    init {
        event<PlayerQuitEvent> { tryRemove(player) }
        event<PlayerKickEvent> { tryRemove(player) }

        registerEvents(plugin)
    }

    fun put(key: Player, value: V, whenPlayerQuit: Player.(V) -> Unit): V? {
        bb.put(key, whenPlayerQuit)
        return put(key, value)
    }

    private fun tryRemove(player: Player) {
        remove(player)?.also {
            bb.remove(player)?.also { block ->
                block.invoke(player, it)
            }
        }
    }
}