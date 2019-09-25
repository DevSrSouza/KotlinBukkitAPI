package br.com.devsrsouza.kotlinbukkitapi.controllers.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.collections.OnlinePlayerMap
import br.com.devsrsouza.kotlinbukkitapi.collections.onlinePlayerMapOf
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.event
import br.com.devsrsouza.kotlinbukkitapi.utils.PlayerLifecycle
import br.com.devsrsouza.kotlinbukkitapi.utils.PlayerLifecycleApplyTest
import br.com.devsrsouza.kotlinbukkitapi.utils.PlayerLifecycleFactory
import br.com.devsrsouza.kotlinbukkitapi.utils.castOrNull
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.valueParameters

internal object PlayerLifecycleController : KListener<KotlinBukkitAPI> {
    override val plugin: KotlinBukkitAPI get() = KotlinBukkitAPI.INSTANCE

    internal data class PlayerLifecycleRegistry(
            val clazz: KClass<PlayerLifecycle<Plugin>>,
            val applyTo: PlayerLifecycleApplyTest,
            val factory: PlayerLifecycleFactory<PlayerLifecycle<Plugin>, Plugin>
    )

    private val playerLifecycles = hashMapOf<String, MutableMap<Player, MutableSet<PlayerLifecycle<Plugin>>>>()

    private val playerLifecyclesRegisters = hashMapOf<Plugin, MutableSet<PlayerLifecycleRegistry>>()

    init {
        event<PluginDisableEvent> {
            playerLifecycles.remove(plugin.name)?.clear()
            playerLifecyclesRegisters.remove(plugin)
        }
        event<PlayerJoinEvent> { joinPlayer(player) }
        event<PlayerQuitEvent> { quitPlayer(player) }
        event<PlayerKickEvent> { quitPlayer(player) }
    }

    private fun joinPlayer(player: Player) {
        for ((plugin, set) in playerLifecyclesRegisters) {
            for (registry in set) {
                registryToPlayer(player, registry, plugin)?.onJoin()
            }
        }
    }

    private fun quitPlayer(player: Player) {
        for (map in playerLifecycles.values) {
            map.remove(player)?.forEach {
                it.onQuit()
                PluginLifecycleController.unregisterLifecycle(it)
            }
        }
    }

    // returns null when PlayerLifecycleApplyTest fails
    private fun registryToPlayer(
            player: Player,
            registry: PlayerLifecycleRegistry,
            plugin: Plugin
    ): PlayerLifecycle<Plugin>? {
        return if (registry.applyTo(player)) registry.factory.create(player, plugin).also {
            playerLifecycles.getOrPut(plugin.name) {
                mutableMapOf()
            }.getOrPut(player) {
                mutableSetOf()
            }.add(it)

            PluginLifecycleController.registerLifecycle(it) // important
        } else null
    }

    fun registerPlayerLifecycle(
            plugin: Plugin,
            playerLifecycle: KClass<PlayerLifecycle<Plugin>>,
            factory: PlayerLifecycleFactory<PlayerLifecycle<Plugin>, Plugin>,
            applyTo: PlayerLifecycleApplyTest
    ) {
        val registry = PlayerLifecycleRegistry(playerLifecycle, applyTo, factory)
        playerLifecyclesRegisters.getOrPut(plugin) { mutableSetOf() }
                .add(registry)

        for (player in Bukkit.getOnlinePlayers()) {
            registryToPlayer(player, registry, plugin)?.onJoin()
        }
    }
}