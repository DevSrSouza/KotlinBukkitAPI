package br.com.devsrsouza.kotlinbukkitapi.plugins.hologramapi

import de.inventivegames.hologram.Hologram
import de.inventivegames.hologram.HologramAPI
import de.inventivegames.hologram.touch.TouchAction
import de.inventivegames.hologram.touch.TouchHandler
import de.inventivegames.hologram.view.ViewHandler
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

val hasHologramAPI by lazy { Bukkit.getServer().pluginManager.getPlugin("HologramAPI") != null  }

val Location.hologramAPI get() = KHologramAPI(this)

inline class KHologramAPI(val location: Location) {
    fun createHologram(text: String) = HologramAPI.createHologram(location, text)
}

fun Hologram.destroy() = HologramAPI.removeHologram(this)

fun Hologram.onView(block: ViewEvent.() -> String) =
    ViewHandler { _, player: Player, text: String -> ViewEvent(player, text).block() }.also { addViewHandler(it) }

fun Hologram.onTouch(block: TouchEvent.() -> Unit) =
    TouchHandler { _, player, touchAction -> TouchEvent(player, touchAction).block() }.also {
        apply { isTouchable = true }.addTouchHandler(it)
    }

class ViewEvent(val player: Player, val text: String)
class TouchEvent(val player: Player, val action: TouchAction)
