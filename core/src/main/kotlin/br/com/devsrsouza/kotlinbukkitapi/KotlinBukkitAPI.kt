package br.com.devsrsouza.kotlinbukkitapi

import br.com.devsrsouza.kotlinbukkitapi.dsl.ChatInputController
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.MenuController
import br.com.devsrsouza.kotlinbukkitapi.extensions.registerEvents
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
        registerEvents(MenuController)
        registerEvents(ChatInputController)
    }
}