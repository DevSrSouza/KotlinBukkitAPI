package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.provideKotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.utils.BungeeCordRequest
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.ByteBuffer
import java.nio.charset.Charset

internal fun provideBungeeCordController() = provideKotlinBukkitAPI().bungeeCordController

internal class BungeeCordController(
        val plugin: KotlinBukkitAPI
) : PluginMessageListener, KBAPIController {

    private val queue = mutableListOf<BungeeCordRequest>()

    override fun onEnable() {
        Bukkit.getServer().messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")
        Bukkit.getServer().messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") return

        val buffer = ByteBuffer.wrap(message)
        val subChannel = buffer.readUTF()
        val request = queue.firstOrNull { it.subChannel == subChannel }
        if(request?.responseCallback != null) {
            val infoBuffer = buffer.slice()
            val info = ByteArray(infoBuffer.remaining())
            infoBuffer.get(info)
            request.responseCallback.invoke(info)
            queue.remove(request)
        }
    }

    fun sendBungeeCord(player: Player, message: ByteArray)
            = player.sendPluginMessage(plugin, "BungeeCord", message)

    fun addToQueue(request: BungeeCordRequest) = queue.add(request)

    private fun ByteBuffer.readUTF() = String(ByteArray(short.toInt()).apply { get(this) }, Charset.forName("UTF-8"))
}
