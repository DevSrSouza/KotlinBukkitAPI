package br.com.devsrsouza.kotlinbukkitapi.server.api.chat

import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player

interface ServerChat {
    fun sendMessage(
            player: Player,
            type: ChatMessageType,
            message: Array<BaseComponent>
    )

    fun sendMessage(
            player: Player,
            type: ChatMessageType,
            message: BaseComponent
    ) = sendMessage(player, type, arrayOf(message))

    fun sendActionBar(
            player: Player,
            message: BaseComponent
    ) = sendMessage(player, ChatMessageType.ACTION_BAR, message)
}

/**
 * https://wiki.vg/Chat#Processing_chat
 */
enum class ChatMessageType(val id: Byte) {
    /**
     * A player-initiated chat message.
     * Note that the Notchian server does not include message-related commands here (/me and /tell);
     * those go in System.
     */
    CHAT(0),

    /**
     * Feedback from running a command,
     * such as "Your game mode has been updated to creative."
     */
    SYSTEM(1),

    /**
     * Game state information that is displayed above the hot bar,
     * such as "You may not rest now, the bed is too far away".
     */
    ACTION_BAR(2)
}
