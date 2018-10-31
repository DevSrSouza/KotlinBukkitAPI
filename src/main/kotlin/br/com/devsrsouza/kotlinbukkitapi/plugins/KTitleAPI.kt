package br.com.devsrsouza.kotlinbukkitapi.plugins.titleapi

import com.connorlinfoot.titleapi.TitleAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

val hasTitleAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("TitleAPI") != null }

val Player.titleAPI get() = KTitleAPI(this)

inline class KTitleAPI(val player: Player) {
    fun clearTitle() = TitleAPI.clearTitle(player)
    fun sendTitle(fadeIn: Long = 20,
                  stay: Long = 40,
                  fadeOut: Long = 20,
                  title: String? = null, subTitle: String? = null) {
        if (title !== null || subTitle !== null)
            TitleAPI.sendTitle(player, fadeIn.toInt(), stay.toInt(), fadeOut.toInt(), title, subTitle)
    }

    fun sendTabTitle(header: String = "", footer: String = "") = TitleAPI.sendTabTitle(player, header, footer)
}