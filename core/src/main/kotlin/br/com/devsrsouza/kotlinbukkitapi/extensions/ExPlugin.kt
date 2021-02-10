package br.com.devsrsouza.kotlinbukkitapi.extensions.plugin

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(
        vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun WithPlugin<*>.registerEvents(
        vararg listeners: Listener
) = plugin.registerEvents(*listeners)

// logger
fun Plugin.info(message: String) = logger.info(message)
fun Plugin.warn(message: String) = logger.warning(message)
fun Plugin.severe(message: String) = logger.severe(message)
fun Plugin.debug(message: String) = logger.config(message)
fun Plugin.fine(message: String) = logger.fine(message)

fun WithPlugin<*>.info(message: String) = plugin.info(message)
fun WithPlugin<*>.warn(message: String) = plugin.warn(message)
fun WithPlugin<*>.severe(message: String) = plugin.severe(message)
fun WithPlugin<*>.debug(message: String) = plugin.debug(message)
fun WithPlugin<*>.fine(message: String) = plugin.fine(message)

interface WithPlugin<T : Plugin> { val plugin: T }
