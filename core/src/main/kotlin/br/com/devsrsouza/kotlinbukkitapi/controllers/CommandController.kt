package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.extensions.command.unregister
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.provideKotlinBukkitAPI
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent

internal fun provideCommandController() = provideKotlinBukkitAPI().commandController

internal class CommandController(
        override val plugin: KotlinBukkitAPI
) : KListener<KotlinBukkitAPI>, KBAPIController {

    val commands = hashMapOf<String, MutableList<Command>>()

    override fun onEnable() {
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach {
                it.unregister()
            }
        }
    }
}