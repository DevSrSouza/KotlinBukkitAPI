package br.com.devsrsouza.kotlinbukkitapi.server.bukkit.v1_11.chat

import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ServerTitle
import org.bukkit.entity.Player

object ServerTitleImpl : ServerTitle {
    override fun sendTitle(player: Player, title: String) {

        player.sendTitle(title, null, -1, -1, -1)
    }

    override fun sendSubtitle(player: Player, subtitle: String) {
        player.sendTitle(null, subtitle, -1, -1, -1)
    }

    override fun sendTitleTime(player: Player, fadeIn: Int, stay: Int, fadeOut: Int) {
        player.sendTitle(null, null, fadeIn, stay, fadeOut)
    }

    override fun resetTitle(player: Player) {
        player.resetTitle()
    }
}