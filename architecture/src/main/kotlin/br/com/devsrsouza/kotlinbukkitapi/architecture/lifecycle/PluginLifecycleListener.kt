package br.com.devsrsouza.kotlinbukkitapi.architecture.lifecycle

public enum class LifecycleEvent { LOAD, ENABLE, DISABLE, CONFIG_RELOAD, ALL_CONFIG_RELOAD }

public typealias PluginLifecycleListener = (LifecycleEvent) -> Unit