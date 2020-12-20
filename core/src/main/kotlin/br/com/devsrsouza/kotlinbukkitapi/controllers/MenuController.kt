package br.com.devsrsouza.kotlinbukkitapi.controllers

import br.com.devsrsouza.kotlinbukkitapi.KotlinBukkitAPI
import br.com.devsrsouza.kotlinbukkitapi.extensions.event.KListener
import br.com.devsrsouza.kotlinbukkitapi.extensions.server.onlinePlayers
import br.com.devsrsouza.kotlinbukkitapi.menu.getMenuFromInventory
import br.com.devsrsouza.kotlinbukkitapi.menu.getMenuFromPlayer
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotInteract
import br.com.devsrsouza.kotlinbukkitapi.menu.slotOrBaseSlot
import br.com.devsrsouza.kotlinbukkitapi.menu.takeIfHasPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.ItemStack

internal class MenuController(
        override val plugin: KotlinBukkitAPI
) : KListener<KotlinBukkitAPI>, KBAPIController {

    override fun onEnable() {
        // do nothing
    }

    @EventHandler
    fun pluginDisableEvent(event: PluginDisableEvent) {
        onlinePlayers.forEach {
            val menu = getMenuFromPlayer(it)
                    ?.takeIf { it.plugin.name == event.plugin.name }
            menu?.close(it, true)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun clickInventoryEvent(event: InventoryClickEvent) {
        if (event.view.type == InventoryType.CHEST) {
            val player = event.whoClicked as Player
            val inv = event.inventory

            val menu = getMenuFromInventory(event.inventory)?.takeIfHasPlayer(player)

            if (menu != null) {
                if (event.rawSlot == event.slot) {
                    val clickedSlot = event.slot + 1
                    val slot = menu.slotOrBaseSlot(clickedSlot)

                    val interact = MenuPlayerSlotInteract(
                            menu, clickedSlot, slot,
                            player, inv, slot.cancel,
                            event.click, event.action,
                            event.currentItem, event.cursor,
                            event.hotbarButton
                    )

                    slot.eventHandler.interact(interact)

                    if (interact.canceled) event.isCancelled = true
                } else {
                    // TODO move item
                    event.isCancelled = menu.cancelOnBottomClick
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun clickInteractEvent(event: InventoryDragEvent) {
        if (event.view.type == InventoryType.CHEST) {
            val player = event.whoClicked as Player
            val menu = getMenuFromInventory(event.inventory)?.takeIfHasPlayer(player)
            if (menu != null) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin)
                {
                    val amount = event.newItems.filter { it.key < event.inventory.size }.map {
                        event.inventory.setItem(it.key, null)
                        it.value.amount
                    }.sum()
                    event.whoClicked.itemOnCursor = ItemStack(event.oldCursor.type, amount + (event.cursor?.amount ?: 0))
                }
            }
        }
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        if (event.view.type == InventoryType.CHEST) {
            val player = event.player as Player
            getMenuFromPlayer(player)?.close(player, false)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPickupItemEvent(event: PlayerPickupItemEvent) {
        if (getMenuFromPlayer(event.player) != null) event.isCancelled = true
    }

    @EventHandler(ignoreCancelled = true)
    fun antiIllegalBottomInventoryDoubleClick(event : InventoryClickEvent)
    {
        if (event.click == ClickType.DOUBLE_CLICK)
        {
            val player = event.whoClicked as Player
            val menu = getMenuFromInventory(event.view.topInventory)?.takeIfHasPlayer(player)
            if (menu != null && !menu.canBottomInventoryDoubleClick)
            {
                val clickedItem = event.cursor
                if (clickedItem != null)
                {
                    if (event.inventory.containsAtLeast(clickedItem, 1))
                    {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun antiIllegalBottomInventoryShiftClick(event : InventoryClickEvent)
    {
        if (event.isShiftClick)
        {
            val player = event.whoClicked as Player
            val menu = getMenuFromInventory(event.inventory)?.takeIfHasPlayer(player)
            if (menu != null && !menu.canBottomInventoryShiftClick)
            {
                event.isCancelled = true
            }
        }
    }
}