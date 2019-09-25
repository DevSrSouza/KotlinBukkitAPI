package br.com.devsrsouza.kotlinbukkitapi

import br.com.devsrsouza.kotlinbukkitapi.controllers.CommandController
import br.com.devsrsouza.kotlinbukkitapi.controllers.lifecycle.PluginLifecycleController
import br.com.devsrsouza.kotlinbukkitapi.controllers.MenuController
import br.com.devsrsouza.kotlinbukkitapi.controllers.PlayerController
import br.com.devsrsouza.kotlinbukkitapi.controllers.lifecycle.PlayerLifecycleController
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.registerEvents
import org.bukkit.plugin.java.JavaPlugin

class KotlinBukkitAPI : JavaPlugin() {

    companion object {
        @JvmStatic lateinit var INSTANCE: KotlinBukkitAPI
            private set
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        registerEvents(
                CommandController,
                MenuController,
                PlayerController,
                PluginLifecycleController,
                PlayerLifecycleController
        )
    }
}