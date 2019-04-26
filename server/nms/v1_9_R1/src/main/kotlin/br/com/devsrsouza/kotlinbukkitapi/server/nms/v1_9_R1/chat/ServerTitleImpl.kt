package br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_9_R1.chat

import br.com.devsrsouza.kotlinbukkitapi.server.api.chat.ServerTitle
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_9_R1.asIChatBaseComponent
import br.com.devsrsouza.kotlinbukkitapi.server.nms.v1_9_R1.sendPacket
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle
import org.bukkit.entity.Player

object ServerTitleImpl : ServerTitle {

    private fun sendAnyTitle(player: Player, action: PacketPlayOutTitle.EnumTitleAction, title: String) {
        val component = TextComponent.fromLegacyText(title).asIChatBaseComponent()

        val packet = PacketPlayOutTitle(action, component)

        player.sendPacket(packet)
    }

    override fun sendTitle(player: Player, title: String) {
        sendAnyTitle(player, PacketPlayOutTitle.EnumTitleAction.TITLE, title)
    }

    override fun sendSubtitle(player: Player, subtitle: String) {
        sendAnyTitle(player, PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitle)
    }

    override fun sendTitleTime(player: Player, fadeIn: Int, stay: Int, fadeOut: Int) {
        val packet = PacketPlayOutTitle(fadeIn, stay, fadeOut)

        player.sendPacket(packet)
    }

    override fun resetTitle(player: Player) {
        val packet = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null)

        player.sendPacket(packet)
    }

}