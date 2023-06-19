package br.com.devsrsouza.kotlinbukkitapi.menu.slot

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

public interface Slot {

    public val item: ItemStack?

    public val eventHandler: SlotEventHandler

    public val slotData: WeakHashMap<String, Any>
    public val playerSlotData: WeakHashMap<Player, WeakHashMap<String, Any>>

    /**
     * Cancel the interaction with this slot
     */
    public var cancel: Boolean

    /**
     * a clone of Slot without slotData and playerSlotData
     */
    public fun clone(item: ItemStack?): Slot

    public fun clearSlotData() {
        slotData.clear()
    }

    public fun clearPlayerData(player: Player) {
        playerSlotData.remove(player)
    }
}