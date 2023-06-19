package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination

import br.com.devsrsouza.kotlinbukkitapi.menu.MenuPlayer
import br.com.devsrsouza.kotlinbukkitapi.menu.MenuPlayerInventory
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.MenuDSL
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination.slot.PaginationSlotDSL
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.slot.SlotDSL
import br.com.devsrsouza.kotlinbukkitapi.utility.collections.ObservableCollection
import org.bukkit.entity.Player
import java.lang.IllegalArgumentException
import java.util.TreeMap
import java.util.WeakHashMap

public typealias ItemsProvider<T> = () -> ObservableCollection<T>
public typealias ItemsAdapter<T> = MenuPlayer.(List<T>) -> List<T>

public enum class Orientation { HORIZONTAL, VERTICAL }

public inline fun <T> MenuPagination<T>.slot(
    builder: PaginationSlotDSL<T>.() -> Unit,
) {
    for (paginationSlot in paginationSlots.values) {
        paginationSlot.builder()
    }
}

public fun MenuDSL.setPlayerOpenPage(player: Player, page: Int) {
    playerData.put(
        player,
        WeakHashMap<String, Any>().apply { put(PAGINATION_OPEN_PAGE_KEY, page) },
    )
}
public inline fun <T> MenuDSL.pagination(
    itemsProvider: ObservableCollection<T>,
    nextPageSlot: SlotDSL,
    previousPageSlot: SlotDSL,
    autoUpdateSwitchPageSlot: Boolean = true,
    startLine: Int = 1,
    endLine: Int = lines - 1,
    startSlot: Int = 1,
    endSlot: Int = 9,
    orientation: Orientation = Orientation.HORIZONTAL,
    noinline itemsAdapterOnOpen: ItemsAdapter<T>? = null,
    noinline itemsAdapterOnUpdate: ItemsAdapter<T>? = null,
    builder: MenuPaginationImpl<T>.() -> Unit,
): MenuPaginationImpl<T> {
    return pagination(
        { itemsProvider },
        nextPageSlot,
        previousPageSlot,
        autoUpdateSwitchPageSlot,
        startLine,
        endLine,
        startSlot,
        endSlot,
        orientation,
        itemsAdapterOnOpen,
        itemsAdapterOnUpdate,
        builder,
    )
}

public inline fun <T> MenuDSL.pagination(
    noinline itemsProvider: ItemsProvider<T>,
    nextPageSlot: SlotDSL,
    previousPageSlot: SlotDSL,
    autoUpdateSwitchPageSlot: Boolean = true,
    startLine: Int = 1,
    endLine: Int = lines - 1,
    startSlot: Int = 1,
    endSlot: Int = 9,
    orientation: Orientation = Orientation.HORIZONTAL,
    noinline itemsAdapterOnOpen: ItemsAdapter<T>? = null,
    noinline itemsAdapterOnUpdate: ItemsAdapter<T>? = null,
    builder: MenuPaginationImpl<T>.() -> Unit,
): MenuPaginationImpl<T> {
    if (startSlot > endSlot) throw IllegalArgumentException()
    if (startLine > endLine) throw IllegalArgumentException()

    return MenuPaginationImpl(
        this,
        itemsProvider,
        nextPageSlot,
        previousPageSlot,
        autoUpdateSwitchPageSlot,
        startLine,
        endLine,
        startSlot,
        endSlot,
        orientation,
        itemsAdapterOnOpen,
        itemsAdapterOnUpdate,
    ).apply(builder)
}

public interface MenuPagination<T> {
    public val menu: MenuDSL
    public val paginationSlots: TreeMap<Int, PaginationSlotDSL<T>>
    public val paginationEventHandler: PaginationEventHandler

    public val itemsProvider: ItemsProvider<T>

    public val nextPageSlot: SlotDSL
    public val previousPageSlot: SlotDSL

    public val autoUpdateSwitchPageSlot: Boolean

    public val startLine: Int
    public val endLine: Int

    public val startSlot: Int
    public val endSlot: Int

    public val orientation: Orientation

    public val itemsAdapterOnOpen: ItemsAdapter<T>?
    public val itemsAdapterOnUpdate: ItemsAdapter<T>?

    public fun onPageChange(pageChange: MenuPlayerPageChangeEvent) {
        paginationEventHandler.pageChangeCallbacks.add(pageChange)
    }

    public fun onPageAvailable(pageAvailable: MenuPlayerPageAvailableEvent) {
        paginationEventHandler.pageAvailableCallbacks.add(pageAvailable)
    }

    public fun hasPreviousPage(player: Player): Boolean

    public fun hasNextPage(player: Player): Boolean

    public fun getPlayerCurrentPage(player: Player): Int

    public fun updateItemsToPlayer(menuPlayer: MenuPlayerInventory)
}
