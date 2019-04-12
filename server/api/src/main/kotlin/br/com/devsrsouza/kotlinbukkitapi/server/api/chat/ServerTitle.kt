package br.com.devsrsouza.kotlinbukkitapi.server.api.chat

import org.bukkit.entity.Player

interface ServerTitle {
    fun sendTitle(
            player: Player,
            title:String
    )

    fun sendSubtitle(
            player: Player,
            subtitle: String
    )

    fun sendTitleTime(
            player: Player,
            fadeIn: Int,
            stay: Int,
            fadeOut: Int
    )

    fun resetTitle(player: Player)

    fun sendTitle(
            player: Player,
            title: String?,
            subtitle: String?,
            fadeIn: Int = 10,
            stay: Int = 70,
            fadeOut: Int = 20
    ) {
        if(title != null) sendTitle(player, title)
        if(subtitle != null) sendSubtitle(player, subtitle)

        sendTitleTime(player, fadeIn, stay, fadeOut)
    }
}