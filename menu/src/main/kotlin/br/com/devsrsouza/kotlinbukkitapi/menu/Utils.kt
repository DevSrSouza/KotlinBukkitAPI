package br.com.devsrsouza.kotlinbukkitapi.menu

import br.com.devsrsouza.kotlinbukkitapi.menu.slot.Slot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

public fun calculateSlot(line: Int, slot: Int): Int = calculateStartLine(line) + slot

public fun calculateStartLine(line: Int): Int = calculateEndLine(line) - 9
public fun calculateEndLine(line: Int): Int = line * 9

public fun rawSlot(slot: Int): Int = slot - 1

public fun Menu<*>.slotOrBaseSlot(slot: Int): Slot = slots[slot] ?: baseSlot
public fun Menu<*>.rangeOfSlots(): IntRange = 1..calculateEndLine(lines)
public fun Menu<*>.viewersFromPlayers(players: Set<Player>): Map<Player, Inventory> = viewers.filterKeys { it in players }

public fun Menu<*>.slotsWithBaseSlot(): List<Slot> = slots.values + baseSlot

public fun Menu<*>.hasPlayer(player: Player): Boolean = viewers.containsKey(player)
public fun Menu<*>.takeIfHasPlayer(player: Player): Menu<*>? = if(hasPlayer(player)) this else null

public fun inventoryIsMenu(inventory: Inventory): Boolean = inventory.holder is Menu<*>

public fun getMenuFromInventory(inventory: Inventory): Menu<*>? = inventory.holder as? Menu<*>

public fun getMenuFromPlayer(player: Player): Menu<*>? {
    return getMenuFromInventory(player.openInventory.topInventory)?.takeIfHasPlayer(player)
}