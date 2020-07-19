package br.com.devsrsouza.kotlinbukkitapi.extensions.bungeecord

import br.com.devsrsouza.kotlinbukkitapi.controllers.provideBungeeCordController
import org.bukkit.entity.Player
import com.google.common.io.ByteStreams
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val Player.bungeecord get() = BungeeCord(this)

fun Player.sendBungeeCord(message: ByteArray) = provideBungeeCordController().sendBungeeCord(this, message)

typealias ResponseCallback = (message: ByteArray) -> Unit

class BungeeCordRequest(
        val player: Player,
        val subChannel: String,
        val request: ByteArray? = null,
        val responseCallback: ResponseCallback? = null
) {
    fun send() {
        val out = ByteStreams.newDataOutput()
        out.writeUTF(subChannel)
        if (request != null) out.write(request)

        player.sendBungeeCord(out.toByteArray())

        if (responseCallback != null) provideBungeeCordController().addToQueue(this)
    }
}

inline class BungeeCord(private val player: Player) {

    fun server(server: String) = BungeeCordRequest(
            player,
            "Connect",
            ByteStreams.newDataOutput().apply{writeUTF(server)}.toByteArray()
    ).send()

    fun ip(callback: (address: String, port: Int) -> Unit) = BungeeCordRequest(player, "IP") {
        val input = ByteStreams.newDataInput(it)
        val ip = input.readUTF()
        val port = input.readInt()
        callback(ip, port)
    }.send()

    suspend fun ip(): Pair<String, Int> = suspendCoroutine {  continuation ->
        ip { address, port ->  continuation.resume(address to port) }
    }

    fun online(server: String = "ALL", callback: (playerCount: Int) -> Unit)
            = BungeeCordRequest(player, "PlayerCount", ByteStreams.newDataOutput().apply{writeUTF(server)}.toByteArray()) {
        val input = ByteStreams.newDataInput(it)
        val server = input.readUTF()
        val playerCount = input.readInt()
        callback(playerCount)
    }.send()

    suspend fun online(server: String = "ALL"): Int = suspendCoroutine { continuation ->
        online {  continuation.resume(it) }
    }

    fun getServerName(callback: (server: String) -> Unit) = BungeeCordRequest(player, "GetServer") {
        val input = ByteStreams.newDataInput(it)
        val server = input.readUTF()
        callback(server)
    }.send()

    suspend fun getServerName(): String = suspendCoroutine { continuation ->
        getServerName {  continuation.resume(it) }
    }

    fun kickPlayer(player: String, reason: String) = BungeeCordRequest(this.player, "KickPlayer",
        ByteStreams.newDataOutput().apply{writeUTF(player);writeUTF(reason)}.toByteArray()).send()
}