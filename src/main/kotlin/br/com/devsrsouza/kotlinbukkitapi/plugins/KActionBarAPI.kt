package br.com.devsrsouza.kotlinbukkitapi.plugins.actionbarapi

import com.connorlinfoot.actionbarapi.ActionBarAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

val hasActionBarAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("ActionBarAPI") != null  }

val Player.actionBarAPI get() = KActionBarAPI(this)

class KActionBarAPI(val player: Player) {
    fun sendActionBar(message: String, duration: Int = 0) = ActionBarAPI.sendActionBar(player, message, duration)
}