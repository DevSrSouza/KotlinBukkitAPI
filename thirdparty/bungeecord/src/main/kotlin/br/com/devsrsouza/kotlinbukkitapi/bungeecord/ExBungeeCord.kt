package br.com.devsrsouza.kotlinbukkitapi.extensions

import br.com.devsrsouza.kotlinbukkitapi.controllers.provideBungeeCordController
import br.com.devsrsouza.kotlinbukkitapi.utils.BungeeCordRequest
import org.bukkit.entity.Player
import com.google.common.io.ByteStreams
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: replace controller with KotlinPlugin lifecycle extension

public val Player.bungeecord get() = BungeeCord(this)

public fun Player.sendBungeeCord(message: ByteArray) = provideBungeeCordController().sendBungeeCord(this, message)

@JvmInline
public value class BungeeCord(private val player: Player) {

    public fun sendToServer(server: String) = BungeeCordRequest(
            player,
            "Connect",
            ByteStreams.newDataOutput().apply{writeUTF(server)}.toByteArray()
    ).send()

    public fun retrieveIp(callback: (address: String, port: Int) -> Unit) = BungeeCordRequest(player, "IP") {
        val input = ByteStreams.newDataInput(it)
        val ip = input.readUTF()
        val port = input.readInt()
        callback(ip, port)
    }.send()

    public suspend fun retrieveIp(): Pair<String, Int> = suspendCoroutine { continuation ->
        retrieveIp { address, port ->  continuation.resume(address to port) }
    }

    public fun onlinePlayerAt(server: String = "ALL", callback: (playerCount: Int) -> Unit)
            = BungeeCordRequest(player, "PlayerCount", ByteStreams.newDataOutput().apply{writeUTF(server)}.toByteArray()) {
        val input = ByteStreams.newDataInput(it)
        val serverSendTo = input.readUTF()
        val playerCount = input.readInt()
        callback(playerCount)
    }.send()

    public suspend fun onlinePlayerAt(server: String = "ALL"): Int = suspendCoroutine { continuation ->
        onlinePlayerAt {  continuation.resume(it) }
    }

    public fun retrieveServerName(callback: (server: String) -> Unit) = BungeeCordRequest(player, "GetServer") {
        val input = ByteStreams.newDataInput(it)
        val server = input.readUTF()
        callback(server)
    }.send()

    public suspend fun retrieveServerName(): String = suspendCoroutine { continuation ->
        retrieveServerName {  continuation.resume(it) }
    }

    public fun kickPlayer(player: String, reason: String) = BungeeCordRequest(this.player, "KickPlayer",
        ByteStreams.newDataOutput().apply{writeUTF(player);writeUTF(reason)}.toByteArray()).send()
}