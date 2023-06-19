package br.com.devsrsouza.kotlinbukkitapi.menu

import br.com.devsrsouza.kotlinbukkitapi.architecture.extensions.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.Slot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin
import java.util.*

public interface Menu<T : Slot> : WithPlugin<Plugin>, InventoryHolder {

    public var title: String
    public var lines: Int
    public var cancelOnClick: Boolean

    public val viewers: Map<Player, Inventory>
    public val slots: TreeMap<Int, T>

    public val data: WeakHashMap<String, Any>
    public val playerData: WeakHashMap<Player, WeakHashMap<String, Any>>

    public val eventHandler: MenuEventHandler

    public var baseSlot: T
    public var updateDelay: Long

    public fun setSlot(slot: Int, slotObj: T)

    public fun update(players: Set<Player> = viewers.keys)
    public fun updateSlot(slot: Slot, players: Set<Player> = viewers.keys)

    public fun update(vararg players: Player): Unit = update(players.toSet())
    public fun updateSlot(slot: Slot, vararg players: Player): Unit = updateSlot(slot, players.toSet())

    public fun openToPlayer(vararg players: Player)

    public fun clearData() {
        data.clear()
        for (slot in slotsWithBaseSlot())
            slot.clearSlotData()
    }

    public fun clearPlayerData(player: Player) {
        playerData.remove(player)
        for (slot in slotsWithBaseSlot())
            slot.clearPlayerData(player)
    }

    public fun close(player: Player, closeInventory: Boolean = true)
}
