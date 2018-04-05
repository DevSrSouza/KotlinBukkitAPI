package br.com.devsrsouza.kotlinbukkitapi.extensions.bungeecord

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import com.google.common.io.ByteStreams
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.ByteBuffer
import java.nio.charset.Charset

val Player.bungeecord get() = BungeeCord(this)

fun Player.sendBungeeCord(message: ByteArray) = BungeeCordController.sendBungeeCord(this, message)

private object BungeeCordController : PluginMessageListener {

    private val queue = mutableListOf<BungeeCordRequest>()

    init {
        Bukkit.getServer().messenger.registerOutgoingPluginChannel(KotlinBukkitAPI.INSTANCE, "BungeeCord")
        Bukkit.getServer().messenger.registerIncomingPluginChannel(KotlinBukkitAPI.INSTANCE, "BungeeCord", this)
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
            = player.sendPluginMessage(KotlinBukkitAPI.INSTANCE, "BungeeCord", message)

    fun addToQueue(request: BungeeCordRequest) = queue.add(request)

    private fun ByteBuffer.readUTF() = String(ByteArray(short.toInt()).apply { get(this) }, Charset.forName("UTF-8"))
}

typealias ResponseCallback = (message: ByteArray) -> Unit

class BungeeCordRequest(val player: Player,
                        val subChannel: String,
                        val request: ByteArray? = null,
                        val responseCallback: ResponseCallback? = null) {
    fun send() {
        val out = ByteStreams.newDataOutput()
        out.writeUTF(subChannel)
        if(request != null) out.write(request)

        player.sendBungeeCord(out.toByteArray())

        if(responseCallback != null) BungeeCordController.addToQueue(this)
    }
}

class BungeeCord(private val player: Player) {

    fun server(name: String) = BungeeCordRequest(player, "Connect").send()

    fun ip(callback: (adress: String, port: Int) -> Unit) = BungeeCordRequest(player, "IP") {
        val input = ByteStreams.newDataInput(it)
        val ip = input.readUTF()
        val port = input.readInt()
        callback(ip, port)
    }.send()


    fun online(server: String = "ALL", callback: (playerCount: Int) -> Unit)
            = BungeeCordRequest(player, "PlayerCount", ByteStreams.newDataOutput().apply{writeUTF(server)}.toByteArray()) {
        val input = ByteStreams.newDataInput(it)
        val server = input.readUTF()
        val playerCount = input.readInt()
        callback(playerCount)
    }.send()

    fun getServerName(callback: (server: String) -> Unit) = BungeeCordRequest(player, "GetServer") {
        val input = ByteStreams.newDataInput(it)
        val server = input.readUTF()
        callback(server)
    }.send()

    fun kickPlayer(player: String, reason: String) = BungeeCordRequest(this.player, "KickPlayer",
        ByteStreams.newDataOutput().apply{writeUTF(player);writeUTF(reason)}.toByteArray()).send()
}