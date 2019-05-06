package br.com.devsrsouza.kotlinbukkitapi.menu.slot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface Slot {

    val item: ItemStack?

    val eventHandler: SlotEventHandler

    val slotData: MutableMap<String, Any>
    val playerSlotData: MutableMap<Player, MutableMap<String, Any>>

    /**
     * Cancel the interaction with this slot
     */
    var cancel: Boolean

    /**
     * a clone of Slot without slotData and playerSlotData
     */
    fun clone(item: ItemStack?): Slot

    fun clearPlayerData(player: Player) {
        playerSlotData.remove(player)
    }
}