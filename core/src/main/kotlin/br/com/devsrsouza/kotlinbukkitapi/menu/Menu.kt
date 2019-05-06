package br.com.devsrsouza.kotlinbukkitapi.menu

import br.com.devsrsouza.kotlinbukkitapi.menu.slot.Slot
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin

interface Menu<T : Slot> : WithPlugin<Plugin>, InventoryHolder {

    var title: String
    var lines: Int
    var cancel: Boolean

    val viewers: Map<Player, Inventory>
    val slots: Map<Int, T>

    val data: MutableMap<String, Any>
    val playerData: MutableMap<Player, MutableMap<String, Any>>

    val eventHandler: MenuEventHandler

    var baseSlot: T
    var updateDelay: Long

    fun setSlot(slot: Int, slotObj: T)

    fun update(vararg players: Player)
    fun updateSlot(slot: Slot, vararg players: Player)

    fun openToPlayer(vararg players: Player)

    fun clearPlayerData(player: Player) {
        playerData.remove(player)
        for(slot in slotsWithBaseSlot())
            slot.clearPlayerData(player)
    }

    fun close(player: Player, closeInventory: Boolean = true)
}