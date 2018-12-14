package br.com.devsrsouza.kotlinbukkitapi.plugins.viaversion

import org.bukkit.entity.Player
import us.myles.ViaVersion.api.Via
import us.myles.ViaVersion.api.ViaAPI

val viaAPI by lazy { Via.getAPI() as ViaAPI<Player> }

val Player.viaVersion get() = KViaVersion(this)

inline class KViaVersion(val player: Player) {
    val playerVersion: Int
        get() = viaAPI.getPlayerVersion(player)
}