package br.com.devsrsouza.kotlinbukkitapi.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

fun calculateSlot(line: Int, slot: Int) = calculateStartLine(line) + slot

fun calculateStartLine(line: Int) = calculateEndLine(line) - 9
fun calculateEndLine(line: Int) = line * 9

fun rawSlot(slot: Int) = slot - 1

fun Menu<*>.slotOrBaseSlot(slot: Int) = slots[slot] ?: baseSlot
fun Menu<*>.rangeOfSlots() = 1..calculateEndLine(lines)
fun Menu<*>.viewersFromPlayers(players: Set<Player>) = viewers.filterKeys { it in players }

fun Menu<*>.slotsWithBaseSlot() = slots.values + baseSlot

fun Menu<*>.hasPlayer(player: Player) = viewers.containsKey(player)
fun Menu<*>.takeIfHasPlayer(player: Player): Menu<*>? = if(hasPlayer(player)) this else null

fun inventoryIsMenu(inventory: Inventory) = inventory.holder is Menu<*>

fun getMenuFromInventory(inventory: Inventory): Menu<*>? = inventory.holder as? Menu<*>

fun getMenuFromPlayer(player: Player): Menu<*>? {
    return getMenuFromInventory(player.openInventory.topInventory)?.takeIfHasPlayer(player)
}