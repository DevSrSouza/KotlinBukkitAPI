package br.com.devsrsouza.kotlinbukkitapi.dsl.player

import br.com.devsrsouza.kotlinbukkitapi.controllers.PlayerController
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

typealias ChatInputCallBack = Player.(String) -> Unit
typealias PlayerCallbackFunction<R> = Player.() -> R
typealias PlayerQuitFunction = PlayerCallbackFunction<Unit>
typealias PlayerMoveFunction = PlayerCallbackFunction<Boolean>

fun Player.chatInput(plugin: Plugin, sync: Boolean = false, callback: ChatInputCallBack) {
    PlayerController.inputCallbacks.put(player, ChatInput(plugin, sync, callback))
}

fun Player.whenQuit(plugin: Plugin, callback: PlayerQuitFunction) {
    PlayerController.functionsQuit.put(this, PlayerCallback(plugin, callback)) {
        it.callback.invoke(player)
    }
}

fun Player.whenMove(plugin: Plugin, callback: PlayerMoveFunction) {
    PlayerController.functionsMove.put(this, PlayerCallback(plugin, callback))
}

class ChatInput(val plugin: Plugin, val sync: Boolean, val callback: ChatInputCallBack)
class PlayerCallback<R>(val plugin: Plugin, val callback: PlayerCallbackFunction<R>)