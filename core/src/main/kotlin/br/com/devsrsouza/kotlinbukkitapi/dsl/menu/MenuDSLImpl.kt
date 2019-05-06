package br.com.devsrsouza.kotlinbukkitapi.dsl.menu

import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.SlotDSL
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.SlotDSLImpl
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.SlotEventHandlerDSL
import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler.task
import br.com.devsrsouza.kotlinbukkitapi.menu.*
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.Slot
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotRender
import br.com.devsrsouza.kotlinbukkitapi.menu.slot.MenuPlayerSlotUpdate
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

inline fun WithPlugin<*>.menu(
        displayName: String,
        lines: Int,
        cancel: Boolean = false,
        block: MenuDSL.() -> Unit
) = plugin.menu(displayName, lines, cancel, block)

inline fun Plugin.menu(
        displayName: String,
        lines: Int,
        cancel: Boolean = false,
        block: MenuDSL.() -> Unit
) = menu(displayName, lines, this, cancel, block)

inline fun menu(
        displayName: String,
        lines: Int,
        plugin: Plugin,
        cancel: Boolean = false,
        block: MenuDSL.() -> Unit
) = MenuDSLImpl(plugin, displayName, lines, cancel).apply(block)

class MenuDSLImpl(
        override val plugin: Plugin,
        override var title: String,
        override var lines: Int,
        override var cancel: Boolean
) : MenuDSL {

    private var task: BukkitTask? = null
    override var updateDelay: Long = 0
        set(value) {
            field = value
            removeTask()
            if (value > 0 && viewers.isNotEmpty())
                setNewTask()
        }

    override val viewers: MutableMap<Player, Inventory> = mutableMapOf()
    override val slots: MutableMap<Int, SlotDSL> = mutableMapOf()

    override val data = mutableMapOf<String, Any>()
    override val playerData = mutableMapOf<Player, MutableMap<String, Any>>()

    override val eventHandler = MenuEventHandlerDSL(this)

    override var baseSlot: SlotDSL = SlotDSLImpl(null, cancel, SlotEventHandlerDSL())

    override fun setSlot(slot: Int, slotObj: SlotDSL) {
        slots.put(slot, slotObj)
    }

    override fun update(vararg players: Player) {
        val viewers = viewersFromPlayers(*players)
        for((player, inventory) in viewers) {
            val update = MenuPlayerUpdate(this, player, inventory, title)
            eventHandler.update(update)

            // TODO title
            /*if(update.title != title)*/

            for(i in rangeOfSlots()) {
                val slot = slotOrBaseSlot(i)
                updateSlotOnlyPos(i, slot, player, inventory)
            }
        }
    }

    override fun updateSlot(slot: Slot, vararg players: Player) {
        val slots: Map<Int, SlotDSL> = if(slot === baseSlot) {
            rangeOfSlots().mapNotNull { if(slots[it] == null) it to slot else null }.toMap()
        } else {
            rangeOfSlots().mapNotNull { if(slot === slots[it]) it to slot else null }.toMap()
        }

        for((player, inventory) in viewersFromPlayers(*players)) {
            for((pos, slot) in slots) {
                updateSlotOnlyPos(pos, slot, player, inventory)
            }
        }
    }

    private fun updateSlotOnlyPos(pos: Int, slot: Slot, player: Player, inventory: Inventory) {
        val slotUpdate = MenuPlayerSlotUpdate(this, pos, slot, player, inventory)
        slot.eventHandler.update(slotUpdate)
    }

    override fun openToPlayer(vararg players: Player) {
        for (player in players) {
            close(player, false)

            try {
                val inv = inventory

                viewers.put(player, inv)

                val preOpen = MenuPlayerPreOpen(this, player)
                eventHandler.preOpen(preOpen)

                if(preOpen.canceled) return

                for (i in rangeOfSlots()) {
                    val slot = slotOrBaseSlot(i)

                    val render = MenuPlayerSlotRender(this, i, slot, player, inv)

                    slot.eventHandler.render(render)
                }

                player.openInventory(inv)

                val open = MenuPlayerOpen(this, player, inventory)
                eventHandler.open(open)

                if (task == null && updateDelay > 0 && viewers.isNotEmpty())
                    setNewTask()

            } catch (e: Throwable) {
                e.printStackTrace()
                removePlayer(player, true)
            }
        }
    }

    override fun getInventory(): Inventory {
        val slots = rangeOfSlots()
        val inventory = Bukkit.createInventory(this, slots.endInclusive, title)

        for(i in slots) {
            val slot = slotOrBaseSlot(i)

            val item = slot.item?.clone()
            inventory.setItem(i - 1, item)
        }

        return inventory
    }

    private fun removePlayer(player: Player, closeInventory: Boolean): Boolean {
        if(closeInventory) player.closeInventory()

        clearPlayerData(player)
        return viewers.remove(player) != null
    }

    override fun close(player: Player, closeInventory: Boolean) {
        if(removePlayer(player, closeInventory)) {
            val menuClose = MenuPlayerClose(this, player)
            eventHandler.close(menuClose)

            if (task != null && updateDelay > 0 && viewers.isEmpty()) {
                removeTask()
            }
        }
    }

    private fun setNewTask() {
        task = task(repeatDelay = updateDelay) { update() }
    }

    private fun removeTask() {
        task?.cancel()
        task = null
    }
}