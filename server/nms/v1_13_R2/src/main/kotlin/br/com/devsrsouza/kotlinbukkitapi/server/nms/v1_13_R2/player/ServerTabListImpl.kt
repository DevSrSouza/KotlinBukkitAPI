package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_13_R2.player

import br.com.devsrsouza.kotlinbukkitapi.server.api.player.ServerTabList
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_13_R2.asIChatBaseComponent
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_13_R2.sendPacket
import net.md_5.bungee.api.chat.BaseComponent
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerListHeaderFooter
import org.bukkit.entity.Player

object ServerTabListImpl : ServerTabList {

    override fun setHeaderFooter(
            player: Player,
            header: Array<BaseComponent>,
            footer: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutPlayerListHeaderFooter()

        packet.header = header.asIChatBaseComponent()
        packet.footer = footer.asIChatBaseComponent()

        player.sendPacket(packet)
    }


    override fun setHeader(
            player: Player,
            header: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutPlayerListHeaderFooter()

        packet.header = header.asIChatBaseComponent()

        player.sendPacket(packet)
    }

    override fun setFooter(
            player: Player,
            footer: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutPlayerListHeaderFooter()

        packet.footer = footer.asIChatBaseComponent()

        player.sendPacket(packet)
    }
}