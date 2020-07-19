package br.com.devsrsouza.kotlinbukkitapi.dsl.menu

import org.bukkit.entity.Player
import java.util.*

fun MenuDSL.putPlayerData(player: Player, key: String, value: Any)
        = playerData.getOrPut(player, { WeakHashMap() }).put(key, value)

fun MenuDSL.getPlayerData(player: Player, key: String): Any?
        = playerData.get(player)?.get(key)
