package br.com.devsrsouza.kotlinbukkitapi.controllers.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.utils.*
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

internal object PluginLifecycleController : KListener<KotlinBukkitAPI> {
    override val plugin: KotlinBukkitAPI get() = KotlinBukkitAPI.INSTANCE

    val lifecycles = hashMapOf<String, MutableList<PluginLifecycle<*>>>()

    init {
        event<PluginDisableEvent> {
            lifecycles.remove(plugin.name)?.forEach {
                it.onDisable()
            }
        }
    }

    fun registerLifecycle(lifecycle: PluginLifecycle<*>) {
        lifecycles.getOrPut(lifecycle.plugin.name) { mutableListOf() }.add(
            lifecycle
        )
    }

    fun unregisterLifecycle(lifecycle: PluginLifecycle<*>) {
        lifecycles.get(lifecycle.plugin.name)?.remove(lifecycle)
    }

    fun reloadLifecycles(plugin: Plugin) {
        lifecycles.get(plugin.name)?.forEach { reloadLifecycle(it) }
    }
}