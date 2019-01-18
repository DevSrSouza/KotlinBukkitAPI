package br.com.devsrsouza.kotlinbukkitapi.extensions.plugin

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(listener: Listener) = server.pluginManager.registerEvents(listener, this)

// logger
fun Plugin.info(message: String) = logger.info(message)
fun Plugin.warn(message: String) = logger.warning(message)
fun Plugin.severe(message: String) = logger.severe(message)
fun Plugin.fine(message: String) = logger.fine(message)