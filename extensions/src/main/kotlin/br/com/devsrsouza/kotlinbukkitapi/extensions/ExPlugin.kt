package br.com.devsrsouza.kotlinbukkitapi.extensions

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

public fun Plugin.registerEvents(
    vararg listeners: Listener,
): Unit = listeners.forEach { server.pluginManager.registerEvents(it, this) }

public fun WithPlugin<*>.registerEvents(
    vararg listeners: Listener,
): Unit = plugin.registerEvents(*listeners)

// logger
public fun Plugin.info(message: String): Unit = logger.info(message)
public fun Plugin.warn(message: String): Unit = logger.warning(message)
public fun Plugin.severe(message: String): Unit = logger.severe(message)
public fun Plugin.debug(message: String): Unit = logger.config(message)
public fun Plugin.fine(message: String): Unit = logger.fine(message)

public fun WithPlugin<*>.info(message: String): Unit = plugin.info(message)
public fun WithPlugin<*>.warn(message: String): Unit = plugin.warn(message)
public fun WithPlugin<*>.severe(message: String): Unit = plugin.severe(message)
public fun WithPlugin<*>.debug(message: String): Unit = plugin.debug(message)
public fun WithPlugin<*>.fine(message: String): Unit = plugin.fine(message)
