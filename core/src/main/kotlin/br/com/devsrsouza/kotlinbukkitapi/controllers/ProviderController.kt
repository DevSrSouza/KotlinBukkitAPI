package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.reflect.KClass

object ProviderController : KListener<KotlinBukkitAPI> {
    override val plugin: KotlinBukkitAPI get() = KotlinBukkitAPI.INSTANCE

    private val providerTree = TreeMap<String, TreeMap<KClass<*>, Any>>()

    fun register(plugin: Plugin, any: Any): Boolean {
        return providerTree.getOrPut(plugin.name, { TreeMap() })
                .putIfAbsent(any::class, any) == null
    }

    fun unregister(plugin: Plugin, any: Any): Boolean {
        return providerTree.get(plugin.name)?.remove(any::class) == true
    }

    fun <T : Any> find(plugin: Plugin, kclass: KClass<T>): T {
        return providerTree.get(plugin.name)?.get(kclass) as T
    }

    init {
        event<PluginDisableEvent> {
            providerTree.remove(plugin.name)
        }
    }
}