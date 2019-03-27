package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.displaced
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.dsl.player.ChatInput
import br.com.devsrsouza.kotlinbukkitapi.dsl.player.PlayerCallback
import br.com.devsrsouza.kotlinbukkitapi.dsl.scheduler.scheduler
import br.com.devsrsouza.kotlinbukkitapi.collections.onlinePlayerMapOf
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal object PlayerController : KListener {
    override val plugin: Plugin get() = KotlinBukkitAPI.INSTANCE

    internal val inputCallbacks = plugin.onlinePlayerMapOf<ChatInput>()
    internal val functionsMove = plugin.onlinePlayerMapOf<PlayerCallback<Boolean>>()
    internal val functionsQuit = plugin.onlinePlayerMapOf<PlayerCallback<Unit>>()

    init {
        event<AsyncPlayerChatEvent>(ignoreCancelled = true) {
            if (message.isNotBlank()) {
                val input = inputCallbacks.remove(player)
                if (input != null) {
                    if (input.sync) scheduler { input.callback(player, message) }.runTask(plugin)
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