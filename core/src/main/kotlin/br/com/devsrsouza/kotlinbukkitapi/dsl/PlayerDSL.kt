package br.com.devsrsouza.kotlinbukkitapi.dsl.player

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.displaced
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler.task
import br.com.devsrsouza.kotlinbukkitapi.utils.OnlinePlayerMap
import br.com.devsrsouza.kotlinbukkitapi.utils.onlinePlayerMapOf
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

typealias ChatInputCallBack = Player.(String) -> Unit
typealias PlayerCallbackFunction<R> = Player.() -> R
typealias PlayerQuitFunction = PlayerCallbackFunction<Unit>
typealias PlayerMoveFunction = PlayerCallbackFunction<Boolean>

fun Player.chatInput(sync: Boolean = false, plugin: Plugin = KotlinBukkitAPI.INSTANCE, callback: ChatInputCallBack) {
    PlayerController.inputCallbacks.put(player, ChatInput(plugin, sync, callback))
}

fun Player.whenQuit(plugin: Plugin = KotlinBukkitAPI.INSTANCE, callback: PlayerQuitFunction) {
    PlayerController.functionsQuit.put(this, PlayerCallback(plugin, callback)) {
        it.callback.invoke(player)
    }
}

fun Player.whenMove(plugin: Plugin = KotlinBukkitAPI.INSTANCE, callback: PlayerMoveFunction) {
    PlayerController.functionsMove.put(this, PlayerCallback(plugin, callback))
}

class ChatInput(val plugin: Plugin, val sync: Boolean, val callback: ChatInputCallBack)
class PlayerCallback<R>(val plugin: Plugin, val callback: PlayerCallbackFunction<R>)

object PlayerController : Listener {

    internal val inputCallbacks = OnlinePlayerMap<ChatInput>()
    internal val functionsMove = onlinePlayerMapOf<PlayerCallback<Boolean>>()
    internal val functionsQuit = onlinePlayerMapOf<PlayerCallback<Unit>>()

    init {
        event<AsyncPlayerChatEvent>(ignoreCancelled = true) {
            if (message.isNotBlank()) {
                val input = inputCallbacks.remove(player)
                if (input != null) {
                    if (input.sync) task { input.callback(player, message) }
                    else input.callback(player, message)
                    isCancelled = true
                }
            }
        }
        event<PlayerMoveEvent>(ignoreCancelled = true) {
            if (displaced) {
                if (functionsMove[player]?.run { callback.invoke(player) } == true) {
                    isCancelled = true
                }
            }
        }
        event<PluginDisableEvent> {
            inputCallbacks.entries.filter { it.value.plugin == plugin }.forEach {
                inputCallbacks.remove(it.key)
            }
            functionsMove.entries.filter { it.value.plugin == plugin }.forEach {
                functionsMove.remove(it.key)
            }
            functionsQuit.entries.filter { it.value.plugin == plugin }.forEach {
                functionsQuit.remove(it.key)
            }
        }
    }
}