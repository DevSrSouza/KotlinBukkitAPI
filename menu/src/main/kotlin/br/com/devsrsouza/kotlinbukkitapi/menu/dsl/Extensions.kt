package br.com.devsrsouza.kotlinbukkitapi.menu.dsl

import org.bukkit.entity.Player
import java.util.*

public fun MenuDSL.putPlayerData(player: Player, key: String, value: Any): Any? = playerData.getOrPut(player, { WeakHashMap() }).put(key, value)

public fun MenuDSL.getPlayerData(player: Player, key: String): Any? = playerData.get(player)?.get(key)
