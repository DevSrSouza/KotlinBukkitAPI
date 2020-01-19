package br.com.devsrsouza.kotlinbukkitapi.menu.slot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

interface Slot {

    val item: ItemStack?

    val eventHandler: SlotEventHandler

    val slotData: WeakHashMap<String, Any>
    val playerSlotData: WeakHashMap<Player, WeakHashMap<String, Any>>

    /**
     * Cancel the interaction with this slot
     */
    var cancel: Boolean

    /**
     * a clone of Slot without slotData and playerSlotData
     */
    fun clone(item: ItemStack?): Slot

    fun clearSlotData() {
        slotData.clear()
    }

    fun clearPlayerData(player: Player) {
        playerSlotData.remove(player)
    }
}