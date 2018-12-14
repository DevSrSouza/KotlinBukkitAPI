package br.com.devsrsouza.kotlinbukkitapi.dsl

import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler.task
import br.com.devsrsouza.kotlinbukkitapi.utils.OnlinePlayerMap
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

typealias ChatInputCallBack = Player.(String) -> Unit

fun chatInput(player: Player, sync: Boolean = false, callback: ChatInputCallBack) {
    ChatInputController.inputCallbacks.put(player, ChatInput(sync, callback))
}

class ChatInput(val sync: Boolean, val callback: ChatInputCallBack)

object ChatInputController : Listener {

    internal val inputCallbacks = OnlinePlayerMap<ChatInput>()

    init {
        event<AsyncPlayerChatEvent> {
            if (message.isNotBlank()) {
                val input = inputCallbacks.remove(player)
                if (input != null) {
                    if (input.sync) task { input.callback(player, message) }
                    else input.callback(player, message)
                    isCancelled = true
                }
            }
        }
    }
}