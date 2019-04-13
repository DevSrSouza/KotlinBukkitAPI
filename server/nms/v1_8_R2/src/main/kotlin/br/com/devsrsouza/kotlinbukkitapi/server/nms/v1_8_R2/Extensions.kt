package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R2

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_8_R2.IChatBaseComponent
import net.minecraft.server.v1_8_R2.Packet
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.sendPacket(packet: Packet<*>) {
    val craftPlayer = player as? CraftPlayer
            ?: throw IllegalArgumentException("Player was to be CraftPlayer.")

    craftPlayer.handle.playerConnection.sendPacket(packet)
}

fun Array<BaseComponent>.asIChatBaseComponent() = IChatBaseComponent.ChatSerializer.a(
        ComponentSerializer.toString(*this)
)