package br.com.devsrsouza.kotlinbukkitapi.dsl.player

import br.com.devsrsouza.kotlinbukkitapi.dsl.event.displaced
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler.task
import br.com.devsrsouza.kotlinbukkitapi.utils.OnlinePlayerMap
import br.com.devsrsouza.kotlinbukkitapi.utils.onlinePlayerListOf
import br.com.devsrsouza.kotlinbukkitapi.utils.onlinePlayerMapOf
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerMoveEvent

typealias ChatInputCallBack = Player.(String) -> Unit
typealias PlayerCallback = Player.() -> Unit
typealias PlayerMoveFunction = Player.() -> Boolean

fun Player.chatInput(sync: Boolean = false, callback: ChatInputCallBack) {
    PlayerController.inputCallbacks.put(player, ChatInput(sync, callback))
}

inline fun Player.whenQuit(crossinline callback: PlayerCallback) {
    PlayerController.functionsQuit.add(this) {
        callback.invoke(player)
    }
}

fun Player.whenMove(callback: PlayerMoveFunction) {
    PlayerController.functionsMove.put(this, callback)
}

class ChatInput(val sync: Boolean, val callback: ChatInputCallBack)

object PlayerController : Listener {

    internal val inputCallbacks = OnlinePlayerMap<ChatInput>()
    internal val functionsMove = onlinePlayerMapOf<PlayerMoveFunction>()
    val functionsQuit = onlinePlayerListOf()

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
            if(displaced) {
                if(functionsMove[player]?.run { invoke(player) } == true) {
                    isCancelled = true
                }
            }
        }
    }
}