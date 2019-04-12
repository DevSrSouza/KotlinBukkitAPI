package br.com.devsrsouza.kotlinbukkitapi.server.extensions

import br.com.devsrsouza.kotlinbukkitapi.server.Server
import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ChatMessageType
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player

fun Player.sendTitle(
        title: String? = null,
        subtitle: String? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
) = Server.Chat.title.sendTitle(this, title, subtitle, fadeIn, stay, fadeOut)

fun Player.resetTitle() = Server.Chat.title.resetTitle(this)

fun Player.sendActionBar(
        message: BaseComponent
) = Server.Chat.chat.sendActionBar(this, message)

fun Player.sendMessage(
        type: ChatMessageType,
        message: BaseComponent
) = Server.Chat.chat.sendMessage(this, type, message)