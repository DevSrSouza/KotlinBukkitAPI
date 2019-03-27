package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.dsl.event.event
import br.com.devsrsouza.kotlinbukkitapi.extensions.command.unregister
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal object CommandController : KListener {
    override val plugin: Plugin get() = KotlinBukkitAPI.INSTANCE

    val commands = hashMapOf<String, MutableList<Command>>()

    init {
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach {
                it.unregister()
            }
        }
    }
}