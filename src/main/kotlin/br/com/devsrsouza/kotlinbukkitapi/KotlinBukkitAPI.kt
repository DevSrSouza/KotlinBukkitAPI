package br.com.devsrsouza.kotlinbukkitapi

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class KotlinBukkitAPI : JavaPlugin(), Listener {

    companion object {
        @JvmStatic lateinit var INSTANCE: KotlinBukkitAPI
            private set
    }

    override fun onLoad() {
        INSTANCE = this
    }
}