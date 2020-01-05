package br.com.devsrsouza.kotlinbukkitapi.extensions.skedule

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import com.okkero.skedule.BukkitDispatcher
import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

fun WithPlugin<*>.schedule(
        initialContext: SynchronizationContext = SynchronizationContext.SYNC,
        co: suspend BukkitSchedulerController.() -> Unit
) = plugin.schedule(initialContext, co)

val BukkitSchedulerController.contextSync get() = SynchronizationContext.SYNC
val BukkitSchedulerController.contextAsync get() = SynchronizationContext.ASYNC

suspend fun BukkitSchedulerController.switchToSync() = switchContext(contextSync)
suspend fun BukkitSchedulerController.switchToAsync() = switchContext(contextAsync)

val WithPlugin<*>.BukkitDispatchers get() = JavaPlugin.getProvidingPlugin(plugin::class.java).BukkitDispatchers
val Plugin.BukkitDispatchers get() = PluginDispatcher(JavaPlugin.getProvidingPlugin(this::class.java))

inline class PluginDispatcher(val plugin: JavaPlugin) {
    val ASYNC get() = BukkitDispatcher(plugin, true)
    val SYNC get() = BukkitDispatcher(plugin, false)
}