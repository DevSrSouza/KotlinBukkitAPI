package br.com.devsrsouza.kotlinbukkitapi.server.api.player

import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player

interface ServerTabList {

    fun setHeaderFooter(
            player: Player,
            header: Array<BaseComponent>,
            footer: Array<BaseComponent>
    )

    fun setHeader(
            player: Player,
            header: Array<BaseComponent>
    )

    fun setFooter(
            player: Player,
            footer: Array<BaseComponent>
    )

}