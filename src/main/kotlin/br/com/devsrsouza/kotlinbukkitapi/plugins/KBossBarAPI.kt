package br.com.devsrsouza.kotlinbukkitapi.plugins.bossbarapi

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.inventivetalent.bossbar.BossBarAPI

val hasBossBarAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("BossBarAPI") != null  }

val Player.bossBarAPI get() = KBossBarAPIPlayer(this)
val Collection<Player>.bossBarAPI get() = KBossBarAPICollection(this)

inline class KBossBarAPIPlayer(val player: Player) {

    fun removeAllBars() = BossBarAPI.removeAllBars(player)

    fun addBar(component: BaseComponent,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(player, component, color, style, progress, *properties)

    fun addBar(component: BaseComponent,
               timeout: Int,
               interval: Long,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(player, component, color, style, progress, timeout, interval, *properties)

    fun addBar(message: String,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(player, TextComponent(*TextComponent.fromLegacyText(message)), color, style, progress, *properties)

    fun addBar(message: String,
               timeout: Int,
               interval: Long,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(player, TextComponent(*TextComponent.fromLegacyText(message)), color, style, progress, timeout, interval, *properties)
}

inline class KBossBarAPICollection(val players: Collection<Player>) {

    fun removeAllBars() = players.forEach { BossBarAPI.removeAllBars(it) }

    fun addBar(component: BaseComponent,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(players, component, color, style, progress, *properties)

    fun addBar(component: BaseComponent,
               timeout: Int,
               interval: Long,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(players, component, color, style, progress, timeout, interval, *properties)

    fun addBar(message: String,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(players, TextComponent(*TextComponent.fromLegacyText(message)), color, style, progress, *properties)

    fun addBar(message: String,
               timeout: Int,
               interval: Long,
               color: BossBarAPI.Color = BossBarAPI.Color.PURPLE,
               style: BossBarAPI.Style = BossBarAPI.Style.PROGRESS,
               progress: Float = 1.0F,
               vararg properties: BossBarAPI.Property = arrayOf())
            = BossBarAPI.addBar(players, TextComponent(*TextComponent.fromLegacyText(message)), color, style, progress, timeout, interval, *properties)
}