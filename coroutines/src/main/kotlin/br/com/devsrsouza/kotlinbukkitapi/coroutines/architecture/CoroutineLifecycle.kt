package br.com.devsrsouza.kotlinbukkitapi.coroutines.architecture

import br.com.devsrsouza.kotlinbukkitapi.architecture.KotlinPlugin
import br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle.LifecycleListener
import br.com.devsrsouza.kotlinbukkitapi.coroutines.architecture.impl.getOrInsertCoroutineLifecycle
import kotlinx.coroutines.*
import org.bukkit.entity.Player

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default.
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * removing the possibility of Job leaks.
 */
public val LifecycleListener<*>.pluginCoroutineScope: CoroutineScope
    get() = plugin.pluginCoroutineScope

public val KotlinPlugin.pluginCoroutineScope: CoroutineScope
    get() = getOrInsertCoroutineLifecycle().pluginCoroutineScope

/**
 * A CoroutineScope that trigger in the Main Thread of Bukkit by default.
 *
 * This scope ensures that your task will be canceled when the plugin disable
 * and when the [player] disconnect the server removing the possibility of Job leaks.
 */
public fun LifecycleListener<*>.playerCoroutineScope(player: Player): CoroutineScope {
    return plugin.playerCoroutineScope(player)
}

public fun KotlinPlugin.playerCoroutineScope(player: Player): CoroutineScope {
    return getOrInsertCoroutineLifecycle().getPlayerCoroutineScope(player)
}
