package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.bukkit.onlinePlayers
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.server.PluginDisableEvent

internal object MenuController : KListener<KotlinBukkitAPI> {
    override val plugin: KotlinBukkitAPI get() = KotlinBukkitAPI.INSTANCE

    internal fun close(player: Player) {
        val menu = (player.openInventory.topInventory.holder as? Menu)?.takeIf { it.viewers.containsKey(player) }
        if (menu != null) {
            menu.close?.invoke(MenuClose(player, player.openInventory.topInventory))
            menu.playerData.remove(player)
            menu.baseSlot.playerSlotData.remove(player)
            menu.slots.forEach { it.value.playerSlotData.remove(player) }
            menu.viewers.remove(player)
            if (menu.task != null && menu.viewers.isNotEmpty()) {
                menu.task?.cancel()
                menu.task = null
            }
        }
    }

    @EventHandler
    fun pluginDisableEvent(event: PluginDisableEvent) {
        onlinePlayers().forEach {
            if(it.openInventory?.topInventory?.holder is Menu)
                it.closeInventory()
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun clickInventoryEvent(event: InventoryClickEvent) {
        if(event.view.type == InventoryType.CHEST && event.inventory.holder is Menu) {
            val player = event.whoClicked as Player
            val menu = (event.inventory.holder as Menu).takeIf { it.viewers.containsKey(player) }
            if(menu != null) {
                if(event.rawSlot == event.slot) {
                    val clickedSlot = event.slot + 1
                    val slot = menu.slots.get(clickedSlot) ?: menu.baseSlot
                    if (slot.click != null) {
                        val slotClick = SlotClick(menu, player,
                                menu.cancel, event.inventory, slot, event.click, event.action,
                                event.currentItem, event.cursor, event.hotbarButton)
                        slot.click?.invoke(slotClick)
                        if (slotClick.cancel) event.isCancelled = true
                    } else {
                        if (menu.cancel) event.isCancelled = true
                    }
                }else{
                    val emptySlot = event.inventory.firstEmpty()
                    if(event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY && emptySlot > -1) {
                        val realEmptySlot = emptySlot + 1
                        val slot = menu.slots.get(realEmptySlot) ?: menu.baseSlot
                        val auxCurrentItem = event.currentItem

                        if(slot.moveToSlot != null) {
                            val moveToSlot = MoveToSlot(player, menu.cancel, auxCurrentItem)
                            slot.moveToSlot?.invoke(moveToSlot)

                            if(moveToSlot.cancel) event.isCancelled = true
                        } else if(menu.moveToMenu != null) {
                            val moveToMenu = MoveToMenu(event.inventory, player,
                                    menu.cancel, realEmptySlot, auxCurrentItem)
                            val auxTargetItem = moveToMenu.targetCurrentItem
                            menu.moveToMenu?.invoke(moveToMenu)

                            val moveItem = moveToMenu.item
                            val moveTargetItem = moveToMenu.targetCurrentItem
                            val exactTargetSlot = moveToMenu.targetSlot - 1

                            if(moveToMenu.cancel) {
                                event.isCancelled = true
                                if(moveItem !== auxCurrentItem) event.currentItem = moveToMenu.item
                                if(moveTargetItem !== auxTargetItem) event.inventory.setItem(exactTargetSlot, moveTargetItem)
                            } else {
                                if(exactTargetSlot != emptySlot) {
                                    //val ref = event.currentItem
                                    event.isCancelled = true
                                    event.currentItem = moveTargetItem
                                    event.inventory.setItem(exactTargetSlot, moveItem)
                                }
                            }
                        }else if (menu.cancel) event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun clickInteractEvent(event: InventoryDragEvent) {
        if (event.view.type == InventoryType.CHEST && event.inventory.holder is Menu) {
            val player = event.whoClicked as Player
            val menu = (event.inventory.holder as Menu).takeIf { it.viewers.containsKey(player) }
            if (menu != null) {
                var pass = 0
                for(i in event.inventorySlots) {
                    for(j in event.rawSlots) {
                        if(i == j) {
                            pass++
                            break
                        }
                    }
                }
                if(pass > 0) event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        if(event.view.type == InventoryType.CHEST) {
            close(event.player as Player)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPickupItemEvent(event: PlayerPickupItemEvent) {
        if (event.player.openInventory.topInventory.holder is Menu) event.isCancelled = true
    }
}