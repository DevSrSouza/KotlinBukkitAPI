package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R1.chat

import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ChatMessageType
import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ServerChat
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R1.sendPacket
import net.md_5.bungee.api.chat.BaseComponent
import net.minecraft.server.v1_8_R1.PacketPlayOutChat
import org.bukkit.entity.Player

object ServerChatImpl : ServerChat {

    override fun sendMessage(
            player: Player,
            type: ChatMessageType,
            message: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutChat(null, type.id).apply {
            components = message
        }

        player.sendPacket(packet)
    }

}