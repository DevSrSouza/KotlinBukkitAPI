package br.com.devsrsouza.kotlinbukkitapi.extensions.plugins

import br.com.devsrsouza.kotlinbukkitapi.extensions.time.second
import com.connorlinfoot.titleapi.TitleAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

val hasTitleAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("TitleAPI") != null }

val Player.titleAPI get() = KTitleAPI(this)

class KTitleAPI(val player: Player) {
    fun clearTitle() = TitleAPI.clearTitle(player)
    fun sendTitle(fadeIn: Long = 1.second.toTick(),
                  stay: Long = 2.second.toTick(),
                  fadeOut: Long = 1.second.toTick(),
                  title: String? = null, subTitle: String? = null) {
        if (title !== null || subTitle !== null)
            TitleAPI.sendTitle(player, fadeIn.toInt(), stay.toInt(), fadeOut.toInt(), title, subTitle)
    }

    fun sendTabTitle(header: String = "", footer: String = "") = TitleAPI.sendTabTitle(player, header, footer)
}