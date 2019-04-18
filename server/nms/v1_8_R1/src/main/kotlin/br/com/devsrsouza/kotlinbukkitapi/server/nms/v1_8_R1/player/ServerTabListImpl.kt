package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R1.player

import br.com.devsrsouza.kotlinbukkitapi.server.api.player.ServerTabList
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R1.asIChatBaseComponent
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_8_R1.sendPacket
import net.md_5.bungee.api.chat.BaseComponent
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter
import org.bukkit.entity.Player

object ServerTabListImpl : ServerTabList {

    val headerField by lazy {
        PacketPlayOutPlayerListHeaderFooter::class.java.getDeclaredField("a").apply {
            isAccessible = true
        }
    }
    val footerField by lazy {
        PacketPlayOutPlayerListHeaderFooter::class.java.getDeclaredField("b").apply {
            isAccessible = true
        }
    }

    override fun setHeaderFooter(
            player: Player,
            header: Array<BaseComponent>,
            footer: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutPlayerListHeaderFooter()

        headerField.set(packet, header.asIChatBaseComponent())
        footerField.set(packet, footer.asIChatBaseComponent())

        player.sendPacket(packet)
    }


    override fun setHeader(
            player: Player,
            header: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutPlayerListHeaderFooter(header.asIChatBaseComponent())

        player.sendPacket(packet)
    }

    override fun setFooter(
            player: Player,
            footer: Array<BaseComponent>
    ) {
        val packet = PacketPlayOutPlayerListHeaderFooter()

        footerField.set(packet, footer.asIChatBaseComponent())

        player.sendPacket(packet)
    }
}