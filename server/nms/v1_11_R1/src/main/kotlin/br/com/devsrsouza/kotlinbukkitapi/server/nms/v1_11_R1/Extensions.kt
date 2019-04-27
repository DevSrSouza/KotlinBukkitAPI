package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_11_R1

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_11_R1.IChatBaseComponent
import net.minecraft.server.v1_11_R1.Packet
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.sendPacket(packet: Packet<*>) {
    val craftPlayer = player as? CraftPlayer
            ?: throw IllegalArgumentException("Player was to be CraftPlayer.")

    craftPlayer.handle.playerConnection.sendPacket(packet)
}

fun Array<BaseComponent>.asIChatBaseComponent() = IChatBaseComponent.ChatSerializer.a(
        ComponentSerializer.toString(*this)
)