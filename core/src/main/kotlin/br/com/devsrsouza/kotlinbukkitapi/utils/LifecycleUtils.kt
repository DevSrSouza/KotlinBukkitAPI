package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.controllers.LifecycleController
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import org.bukkit.plugin.Plugin

interface PluginLifecycle<T : Plugin> : WithPlugin<T> {
    fun onDisable()
    fun onReload() {}
}

fun registerLifecycle(lifecycle: PluginLifecycle<*>) {
    LifecycleController.registerLifecycle(lifecycle)
}

fun  unregisterLifecycle(lifecycle: PluginLifecycle<*>) {
    LifecycleController.unregisterLifecycle(lifecycle)
}

fun reloadLifecycle(lifecycle: PluginLifecycle<*>) {
    lifecycle.onReload()
}

fun Plugin.reloadLifecycles() {
    LifecycleController.reloadLifecycles(this)
}