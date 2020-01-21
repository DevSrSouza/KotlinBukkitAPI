package br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination.slot

import br.com.devsrsouza.kotlinbukkitapi.menu.Menu
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.*
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

typealias MenuPlayerSlotPageChangeEvent<T> = MenuPlayerSlotPageChange.(T?) -> Unit
typealias MenuPlayerPageSlotInteractEvent<T> = MenuPlayerSlotInteract.(T?) -> Unit
typealias MenuPlayerPageSlotRenderEvent<T> = MenuPlayerSlotRender.(T?) -> Unit
typealias MenuPlayerPageSlotUpdateEvent<T> = MenuPlayerSlotUpdate.(T?) -> Unit

class PaginationSlotEventHandler<T> {
    val pageChangeCallbacks = mutableListOf<MenuPlayerSlotPageChangeEvent<T>>()
    val interactCallbacks = mutableListOf<MenuPlayerPageSlotInteractEvent<T>>()
    val renderCallbacks = mutableListOf<MenuPlayerPageSlotRenderEvent<T>>()
    val updateCallbacks = mutableListOf<MenuPlayerPageSlotUpdateEvent<T>>()

    fun handlePageChange(currentItem: T?, pageChange: MenuPlayerSlotPageChange) {
        for (callback in pageChangeCallbacks) {
            callback(pageChange, currentItem)
        }
    }

    fun handleRender(currentItem: T?, render: MenuPlayerSlotRender) {
        for (callback in renderCallbacks) {
            callback(render, currentItem)
        }
    }

    fun handleUpdate(currentItem: T?, update: MenuPlayerSlotUpdate) {
        for (callback in updateCallbacks) {
            callback(update, currentItem)
        }
    }

    fun handleInteract(currentItem: T?, interact: MenuPlayerSlotInteract) {
        for (callback in interactCallbacks) {
            callback(interact, currentItem)
        }
    }
}

class MenuPlayerSlotPageChange(
        override val menu: Menu<*>,
        override val slotPos: Int,
        override val slot: Slot,
        override val player: Player,
        override val inventory: Inventory
) : MenuPlayerInventorySlot