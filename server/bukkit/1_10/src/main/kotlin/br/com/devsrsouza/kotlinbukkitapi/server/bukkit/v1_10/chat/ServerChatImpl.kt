package br.com.devsrsouza.kotlinbukkitapi.server.bukkit.v1_10.chat

import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ChatMessageType
import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ServerChat
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.ChatMessageType as BungeeMessageType
import org.bukkit.entity.Player

object ServerChatImpl : ServerChat {

    override fun sendMessage(
            player: Player,
            type: ChatMessageType,
            message: Array<BaseComponent>
    ) {
        player.spigot().sendMessage(
                when(type) {
                    ChatMessageType.CHAT -> BungeeMessageType.CHAT
                    ChatMessageType.SYSTEM -> BungeeMessageType.SYSTEM
                    ChatMessageType.ACTION_BAR -> BungeeMessageType.ACTION_BAR
                }, *message
        )
    }
}