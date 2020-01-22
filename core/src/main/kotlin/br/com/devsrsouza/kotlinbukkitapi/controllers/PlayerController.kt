package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.collections.onlinePlayerMapOf
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.displaced
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler.scheduler
import br.com.devsrsouza.kotlinbukkitapi.provideKotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.utils.player.ChatInput
import br.com.devsrsouza.kotlinbukkitapi.utils.player.PlayerCallback
import br.com.devsrsouza.kotlinbukkitapi.utils.provider
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal fun providePlayerController() = provideKotlinBukkitAPI().playerController

internal class PlayerController(
        override val plugin: KotlinBukkitAPI
) : KListener<KotlinBukkitAPI>, KBAPIController {

    internal val inputCallbacks by lazy { plugin.onlinePlayerMapOf<ChatInput>() }
    internal val functionsMove by lazy { plugin.onlinePlayerMapOf<PlayerCallback<Boolean>>() }
    internal val functionsQuit by lazy { plugin.onlinePlayerMapOf<PlayerCallback<Unit>>() }

    override fun onEnable() {
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