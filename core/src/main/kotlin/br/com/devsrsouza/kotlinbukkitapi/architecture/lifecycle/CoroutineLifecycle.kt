package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.impl.CoroutineLifecycle
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.impl.getOrInsertCoroutineLifecycle
import br.com.devsrsouza.kotlinbukkitapi.collections.onlinePlayerMapOf
import br.com.devsrsouza.kotlinbukkitapi.extensions.skedule.BukkitDispatchers
import kotlinx.coroutines.*
import org.bukkit.entity.Player

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default.
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * removing the possibility of Job leaks.
 */
val LifecycleListener<*>.pluginCoroutineScope: CoroutineScope
    get() = plugin.pluginCoroutineScope

val KotlinPlugin.pluginCoroutineScope: CoroutineScope
    get() = getOrInsertCoroutineLifecycle().pluginCoroutineScope

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default.
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * and when the [player] disconnect the server removing the possibility of Job leaks.
 */
fun LifecycleListener<*>.playerCoroutineScope(player: Player): CoroutineScope {
    return plugin.playerCoroutineScope(player)
}

fun KotlinPlugin.playerCoroutineScope(player: Player): CoroutineScope {
    return getOrInsertCoroutineLifecycle().getPlayerCoroutineScope(player)
}
