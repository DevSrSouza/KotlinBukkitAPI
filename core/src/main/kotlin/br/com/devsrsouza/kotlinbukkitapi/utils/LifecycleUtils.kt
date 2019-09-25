package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.controllers.lifecycle.PlayerLifecycleController
import br.com.devsrsouza.kotlinbukkitapi.controllers.lifecycle.PluginLifecycleController
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.valueParameters

interface PluginLifecycle<T : Plugin> : WithPlugin<T> {
    /**
     * Called when the Plugin disable like: Server Stop,
     * Reload Server or Plugins such Plugman disable the plugin.
     */
    fun onDisable()

    /**
     * Called when you forces a "fake reload" for the
     * lifecycle using [reloadLifecycle] or [reloadLifecycles].
     *
     * Usage:
     * When you reload the configuration, and you want to
     * your managers updates their values.
     */
    fun onReload() {}
}

fun registerLifecycle(lifecycle: PluginLifecycle<*>) {
    PluginLifecycleController.registerLifecycle(lifecycle)
}

fun  unregisterLifecycle(lifecycle: PluginLifecycle<*>) {
    PluginLifecycleController.unregisterLifecycle(lifecycle)
}

fun reloadLifecycle(lifecycle: PluginLifecycle<*>) {
    lifecycle.onReload()
}

fun Plugin.reloadLifecycles() {
    PluginLifecycleController.reloadLifecycles(this)
}

typealias PlayerLifecycleApplyTest = (Player) -> Boolean

abstract class PlayerLifecycle<T : Plugin>(
        val player: Player,
        override val plugin: T
) : PluginLifecycle<T> {

    /**
     * Called when a player join the server or when registred and the server has player.
     */
    abstract fun onJoin()

    /**
     * Called when the player quit the server
     */
    abstract fun onQuit()

    /**
     * Called when the Plugin disable like: Server Stop,
     * Reload Server or Plugins such Plugman disable the plugin.
     */
    override fun onDisable() {}
}

interface PlayerLifecycleFactory<T : PlayerLifecycle<P>, P : Plugin> {
    /**
     * Creates a new instance of yor PlayerLifecycle([T])
     */
    fun create(player: Player, plugin: P): T
}

fun <T : PlayerLifecycle<P>, P : Plugin> P.registerPlayerLifecycle(
        playerLifecycle: KClass<T>,
        factory: PlayerLifecycleFactory<T, P> = DefaultPlayerLifecycleFactory(playerLifecycle),
        applyTo: PlayerLifecycleApplyTest = { true }
) {
    PlayerLifecycleController.registerPlayerLifecycle(
            this,
            playerLifecycle as KClass<PlayerLifecycle<Plugin>>,
            factory as PlayerLifecycleFactory<PlayerLifecycle<Plugin>, Plugin>,
            applyTo
    )
}

inline fun <reified T : PlayerLifecycle<P>, P : Plugin> P.registerPlayerLifecycle(
        factory: PlayerLifecycleFactory<T, P> = DefaultPlayerLifecycleFactory(T::class),
        noinline applyTo: PlayerLifecycleApplyTest
) {
    registerPlayerLifecycle(T::class, factory, applyTo)
}

class DefaultPlayerLifecycleFactory<T : PlayerLifecycle<P>, P : Plugin>(
        val typeClass: KClass<T>
) : PlayerLifecycleFactory<T, P> {
    override fun create(player: Player, plugin: P): T {
        return typeClass.constructors.find {
            val isPlayer = it.valueParameters.argumentIsSubclassOf<Player>(0)
            val isPlugin = it.valueParameters.argumentIsSubclassOf<Plugin>(1)

            isPlayer && isPlugin
        }?.call(player, plugin) ?: throw IllegalArgumentException(
                "Could not find the constructor with player, plugin for ${typeClass.qualifiedName}"
        )
    }

    private inline fun <reified T> List<KParameter>.argumentIsSubclassOf(
            index: Int
    ) = getOrNull(index)
            ?.type
            ?.classifier
            ?.castOrNull<KClass<*>?>()
            ?.isSubclassOf(T::class) ?: false
}