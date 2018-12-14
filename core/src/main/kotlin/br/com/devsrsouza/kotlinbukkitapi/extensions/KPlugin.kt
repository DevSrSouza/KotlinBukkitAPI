package br.com.devsrsouza.kotlinbukkitapi.extensions

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(listener: Listener) = server.pluginManager.registerEvents(listener, this)