package br.com.devsrsouza.kotlinbukkitapi.server.extensions

import br.com.devsrsouza.kotlinbukkitapi.server.Server
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player

fun Player.setHeaderFooter(
        header: Array<BaseComponent>,
        footer: Array<BaseComponent>
) = Server.tabList.setHeaderFooter(this, header, footer)

fun Player.setHeaderFooter(
        header: BaseComponent,
        footer: BaseComponent
) = setHeaderFooter(arrayOf(header), arrayOf(footer))

fun Player.setHeader(
        header: Array<BaseComponent>
) = Server.tabList.setHeader(this, header)

fun Player.setHeader(
        header: BaseComponent
) = setHeader(arrayOf(header))

fun Player.setFooter(
        footer: Array<BaseComponent>
) = Server.tabList.setFooter(this, footer)

fun Player.setFooter(
        footer: BaseComponent
) = setFooter(arrayOf(footer))