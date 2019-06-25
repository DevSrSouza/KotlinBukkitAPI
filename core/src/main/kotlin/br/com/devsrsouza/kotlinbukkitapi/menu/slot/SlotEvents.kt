package br.com.devsrsouza.kotlinbukkitapi.menu.slot

import br.com.devsrsouza.kotlinbukkitapi.menu.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

val MenuPlayerSlot.rawSlotPos get() = slotPos - 1

interface MenuPlayerSlot : MenuPlayer {
    val slotPos: Int
    val slot: Slot

    fun putPlayerSlotData(key: String, value: Any) {
        slot.playerSlotData.getOrPut(player) {
            mutableMapOf()
        }[key] = value
    }

    fun getPlayerSlotData(key: String): Any? = slot.playerSlotData.get(player)?.get(key)
}

interface MenuPlayerInventorySlot : MenuPlayerSlot, MenuPlayerInventory {

    var showingItem: ItemStack?
        get() = getItem(slotPos)?.takeUnless { it.type == Material.AIR }
        set(value) = setItem(slotPos, value)

    fun updateSlotToPlayer() {
        menu.updateSlot(slot, player)
    }

    fun updateSlot() {
        menu.updateSlot(slot)
    }
}

open class MenuPlayerSlotInteract(
        menu: Menu<*>,
        override val slotPos: Int,
        override val slot: Slot,
        player: Player,
        inventory: Inventory,
        canceled: Boolean,
        val click: ClickType,
        val action: InventoryAction,
        val clicked: ItemStack?,
        val cursor: ItemStack?,
        val hotbarKey: Int
) : MenuPlayerInteract(menu, player, inventory, canceled), MenuPlayerInventorySlot

class MenuPlayerSlotMoveTo(
        override val menu: Menu<*>,
        override val slotPos: Int,
        override val slot: Slot,
        override val player: Player,
        override val inventory: Inventory,
        override var canceled: Boolean,
        override val toMoveSlot: Int,
        override var toMoveItem: ItemStack?
) : MenuPlayerInventorySlot, MenuPlayerMove

class MenuPlayerSlotRender(
        override val menu: Menu<*>,
        override val slotPos: Int,
        override val slot: Slot,
        override val player: Player,
        override val inventory: Inventory
) : MenuPlayerInventorySlot

class MenuPlayerSlotUpdate(
        override val menu: Menu<*>,
        override val slotPos: Int,
        override val slot: Slot,
        override val player: Player,
        override val inventory: Inventory
) : MenuPlayerInventorySlot