package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination.slot

import br.com.devsrsouza.kotlinbukkitapi.menu.Menu
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerInventorySlot
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotInteract
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotRender
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotUpdate
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.Slot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

public typealias MenuPlayerSlotPageChangeEvent<T> = MenuPlayerSlotPageChange.(T?) -> Unit
public typealias MenuPlayerPageSlotInteractEvent<T> = MenuPlayerSlotInteract.(T?) -> Unit
public typealias MenuPlayerPageSlotRenderEvent<T> = MenuPlayerSlotRender.(T?) -> Unit
public typealias MenuPlayerPageSlotUpdateEvent<T> = MenuPlayerSlotUpdate.(T?) -> Unit

public class PaginationSlotEventHandler<T> {
    internal val pageChangeCallbacks = mutableListOf<MenuPlayerSlotPageChangeEvent<T>>()
    internal val interactCallbacks = mutableListOf<MenuPlayerPageSlotInteractEvent<T>>()
    internal val renderCallbacks = mutableListOf<MenuPlayerPageSlotRenderEvent<T>>()
    internal val updateCallbacks = mutableListOf<MenuPlayerPageSlotUpdateEvent<T>>()

    public fun handlePageChange(currentItem: T?, pageChange: MenuPlayerSlotPageChange) {
        for (callback in pageChangeCallbacks) {
            callback(pageChange, currentItem)
        }
    }

    public fun handleRender(currentItem: T?, render: MenuPlayerSlotRender) {
        for (callback in renderCallbacks) {
            callback(render, currentItem)
        }
    }

    public fun handleUpdate(currentItem: T?, update: MenuPlayerSlotUpdate) {
        for (callback in updateCallbacks) {
            callback(update, currentItem)
        }
    }

    public fun handleInteract(currentItem: T?, interact: MenuPlayerSlotInteract) {
        for (callback in interactCallbacks) {
            callback(interact, currentItem)
        }
    }
}

public class MenuPlayerSlotPageChange(
    override val menu: Menu<*>,
    override val slotPos: Int,
    override val slot: Slot,
    override val player: Player,
    override val inventory: Inventory,
) : MenuPlayerInventorySlot
