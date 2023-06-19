package br.com.devsrsouza.kotlinbukkitapi.architecture.extensions

import org.bukkit.plugin.Plugin

// TODO: make it a experimental API that will be replaced with Context Receivers in the future
public interface WithPlugin<T : Plugin> { public val plugin: T }
