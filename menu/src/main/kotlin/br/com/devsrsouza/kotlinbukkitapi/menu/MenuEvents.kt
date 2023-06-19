package br.com.devsrsouza.kotlinbukkitapi.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

public interface MenuPlayer {
    public val menu: Menu<*>
    public val player: Player

    public fun putPlayerData(key: String, value: Any) {
        val map = menu.playerData[player]
        if (map != null) map[key] = value
        else menu.playerData[player] = WeakHashMap<String, Any>().apply {put(key, value)}
    }

    public fun getPlayerData(key: String): Any? = menu.playerData.get(player)?.get(key)
}

public interface MenuPlayerInventory : MenuPlayer {
    public val inventory: Inventory

    public fun close(): Unit = player.closeInventory()

    public fun getItem(line: Int, slot: Int): ItemStack? {
        return getItem(calculateSlot(line, slot))
    }

    public fun getItem(slot: Int): ItemStack? = inventory.getItem(slot - 1)

    public fun setItem(line: Int, slot: Int, item: ItemStack?) {
        setItem(calculateSlot(line, slot), item)
    }

    public fun setItem(slot: Int, item: ItemStack?) {
        inventory.setItem(rawSlot(slot), item)
    }

    public fun getPlayerItem(line: Int, slot: Int): ItemStack? {
        return getPlayerItem(calculateSlot(line, slot))
    }

    public fun getPlayerItem(slot: Int): ItemStack? = player.inventory.getItem(rawSlot(slot))

    public fun setPlayerItem(line: Int, slot: Int, item: ItemStack?) {
        setPlayerItem(calculateSlot(line, slot), item)
    }

    public fun setPlayerItem(slot: Int, item: ItemStack?) {
        player.inventory.setItem(rawSlot(slot), item)
    }

    private fun getLineRange(line: Int) = calculateSlot(line, 1)..calculateEndLine(line)

    public fun getLine(line: Int): List<ItemStack?> = getLineRange(line).map { getItem(it) }

    public fun getPlayerLine(line: Int): List<ItemStack?> = getLineRange(line).map { getPlayerItem(it) }

    public fun updateToPlayer() {
        menu.update(player)
    }
}

public interface MenuPlayerCancellable : MenuPlayer {
    public var canceled: Boolean
}

public open class MenuPlayerInteract(
    override val menu: Menu<*>,
    override val player: Player,
    override val inventory: Inventory,
    override var canceled: Boolean
) : MenuPlayerInventory, MenuPlayerCancellable

public class MenuPlayerPreOpen(
    override val menu: Menu<*>,
    override val player: Player,
    override var canceled: Boolean = false
) : MenuPlayerCancellable

public class MenuPlayerOpen(
    override val menu: Menu<*>,
    override val player: Player,
    override val inventory: Inventory
) : MenuPlayerInventory

public class MenuPlayerUpdate(
    override val menu: Menu<*>,
    override val player: Player,
    override val inventory: Inventory,
    public var title: String
) : MenuPlayerInventory

public class MenuPlayerClose(
    override val menu: Menu<*>,
    override val player: Player
) : MenuPlayer

public interface MenuPlayerMove : MenuPlayerInventory, MenuPlayerCancellable {
    public val toMoveSlot: Int
    public val toMoveItem: ItemStack?
}

public class MenuPlayerMoveTo(
    menu: Menu<*>,
    player: Player,
    inventory: Inventory,
    canceled: Boolean,
    override val toMoveSlot: Int,
    override val toMoveItem: ItemStack?,
    public var targetSlot: Int
) : MenuPlayerInteract(menu, player, inventory, canceled), MenuPlayerMove {

    public var targetCurrentItem: ItemStack?
        set(value) {
            setItem(targetSlot, value)
        }
        get() = getItem(targetSlot)

}