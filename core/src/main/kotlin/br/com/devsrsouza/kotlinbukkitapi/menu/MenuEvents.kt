package br.com.devsrsouza.kotlinbukkitapi.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface MenuPlayer {
    val menu: Menu<*>
    val player: Player

    fun putPlayerData(key: String, value: Any) {
        val map = menu.playerData[player]
        if (map != null) map[key] = value
        else menu.playerData[player] = mutableMapOf(key to value)
    }

    fun getPlayerData(key: String) = menu.playerData.get(player)?.get(key)
}

interface MenuPlayerInventory : MenuPlayer {
    val inventory: Inventory

    fun close() = player.closeInventory()

    fun getItem(line: Int, slot: Int): ItemStack? {
        return getItem(calculateSlot(line, slot))
    }

    fun getItem(slot: Int): ItemStack? = inventory.getItem(slot - 1)

    fun setItem(line: Int, slot: Int, item: ItemStack?) {
        setItem(calculateSlot(line, slot), item)
    }

    fun setItem(slot: Int, item: ItemStack?) {
        inventory.setItem(rawSlot(slot), item)
    }

    fun getPlayerItem(line: Int, slot: Int): ItemStack? {
        return getPlayerItem(calculateSlot(line, slot))
    }

    fun getPlayerItem(slot: Int): ItemStack? = player.inventory.getItem(rawSlot(slot))

    fun setPlayerItem(line: Int, slot: Int, item: ItemStack?) {
        setPlayerItem(calculateSlot(line, slot), item)
    }

    fun setPlayerItem(slot: Int, item: ItemStack?) {
        player.inventory.setItem(rawSlot(slot), item)
    }

    private fun getLineRange(line: Int) = calculateSlot(line, 1)..calculateEndLine(line)

    fun getLine(line: Int): List<ItemStack?> = getLineRange(line).map { getItem(it) }

    fun getPlayerLine(line: Int): List<ItemStack?> = getLineRange(line).map { getPlayerItem(it) }

    fun updateToPlayer() {
        menu.update(player)
    }
}

interface MenuPlayerCancellable : MenuPlayer {
    var canceled: Boolean
}

open class MenuPlayerInteract(
        override val menu: Menu<*>,
        override val player: Player,
        override val inventory: Inventory,
        override var canceled: Boolean
) : MenuPlayerInventory, MenuPlayerCancellable

class MenuPlayerPreOpen(
        override val menu: Menu<*>,
        override val player: Player,
        override var canceled: Boolean = false
) : MenuPlayerCancellable

class MenuPlayerOpen(
        override val menu: Menu<*>,
        override val player: Player,
        override val inventory: Inventory
) : MenuPlayerInventory

class MenuPlayerUpdate(
        override val menu: Menu<*>,
        override val player: Player,
        override val inventory: Inventory,
        var title: String
) : MenuPlayerInventory

class MenuPlayerClose(
        override val menu: Menu<*>,
        override val player: Player
) : MenuPlayer

interface MenuPlayerMove : MenuPlayerInventory, MenuPlayerCancellable {
    val toMoveSlot: Int
    val toMoveItem: ItemStack?
}

class MenuPlayerMoveTo(
        menu: Menu<*>,
        player: Player,
        inventory: Inventory,
        canceled: Boolean,
        override val toMoveSlot: Int,
        override val toMoveItem: ItemStack?,
        var targetSlot: Int
) : MenuPlayerInteract(menu, player, inventory, canceled), MenuPlayerMove {

    var targetCurrentItem: ItemStack?
        set(value) {
            setItem(targetSlot, value)
        }
        get() = getItem(targetSlot)

}