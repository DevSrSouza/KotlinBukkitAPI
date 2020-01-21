package br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination

import br.com.devsrsouza.kotlinbukkitapi.collections.ObservableList
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.MenuDSL
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.pagination.slot.PaginationSlotDSL
import br.com.devsrsouza.kotlinbukkitapi.dsl.menu.slot.SlotDSL
import br.com.devsrsouza.kotlinbukkitapi.menu.MenuPlayer
import br.com.devsrsouza.kotlinbukkitapi.menu.MenuPlayerInventory
import org.bukkit.entity.Player
import java.util.*

typealias ItemsAdapter<T> = MenuPlayer.(List<T>) -> List<T>

enum class Orientation { HORIZONTAL, VERTICAL }

inline fun <T> MenuPagination<T>.slot(
        builder: PaginationSlotDSL<T>.() -> Unit
) {
    for (paginationSlot in paginationSlots.values) {
        paginationSlot.builder()
    }
}

fun MenuDSL.setPlayerOpenPage(player: Player, page: Int) {
    playerData.put(
            player,
            WeakHashMap<String, Any>().apply { put(PAGINATION_OPEN_PAGE_KEY, page) }
    )
}

interface MenuPagination<T> {
    val menu: MenuDSL
    val paginationSlots: TreeMap<Int, PaginationSlotDSL<T>>
    val paginationEventHandler: PaginationEventHandler

    val items: ObservableList<T>

    val nextPageSlot: SlotDSL
    val previousPageSlot: SlotDSL

    val startLine: Int
    val endLine: Int

    val startSlot: Int
    val endSlot: Int

    val orientation: Orientation

    val itemsAdapterOnOpen: ItemsAdapter<T>?
    val itemsAdapterOnUpdate: ItemsAdapter<T>?

    fun onPageChange(pageChange: MenuPlayerPageChangeEvent) {
        paginationEventHandler.pageChangeCallbacks.add(pageChange)
    }

    fun onPageAvailable(pageAvailable: MenuPlayerPageAvailableEvent) {
        paginationEventHandler.pageAvailableCallbacks.add(pageAvailable)
    }

    fun hasPreviousPage(player: Player): Boolean

    fun hasNextPage(player: Player): Boolean

    fun getPlayerCurrentPage(player: Player): Int

    fun updateItemsToPlayer(menuPlayer: MenuPlayerInventory)
}