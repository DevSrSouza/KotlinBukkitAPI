package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination

import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.MenuDSL
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination.slot.PaginationSlotDSL
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination.slot.PaginationSlotDSLImpl
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.slot
import br.com.devsrsouza.kotlinbukkitapi.menu.dsl.slot.SlotDSL
import br.com.devsrsouza.kotlinbukkitapi.menu.MenuPlayerInventory
import br.com.devsrsouza.kotlinbukkitapi.menu.calculateSlot
import org.bukkit.entity.Player
import java.util.*

internal const val PAGINATION_OPEN_PAGE_KEY = "PAGINATION:open_page"

public class MenuPaginationImpl<T>(
    override val menu: MenuDSL,
    override val itemsProvider: ItemsProvider<T>,
    override val nextPageSlot: SlotDSL,
    override val previousPageSlot: SlotDSL,
    override val autoUpdateSwitchPageSlot: Boolean,
    override val startLine: Int = 1,
    override val endLine: Int = menu.lines - 1,
    override val startSlot: Int = 1,
    override val endSlot: Int = 9,
    override val orientation: Orientation = Orientation.HORIZONTAL,
    override val itemsAdapterOnOpen: ItemsAdapter<T>? = null,
    override val itemsAdapterOnUpdate: ItemsAdapter<T>? = null
) : MenuPagination<T> {
    override val paginationEventHandler: PaginationEventHandler = PaginationEventHandler()

    override val paginationSlots: TreeMap<Int, PaginationSlotDSL<T>> = TreeMap()
    internal val currentPlayerPage = WeakHashMap<Player, Int>()

    // should be null when is not mapped (for not allocate a new list everytime)
    internal val currentPlayerItems = WeakHashMap<Player, List<T>>()

    internal val itemSlotData = WeakHashMap<T, WeakHashMap<String, Any>>()
    internal val itemPlayerSlotData = WeakHashMap<T, WeakHashMap<Player, WeakHashMap<String, Any>>>()

    init {
        // setup next page button click
        nextPageSlot.onClick {
            if(hasNextPage(player))
                nextPage(this)
        }

        // setup previous page button click
        previousPageSlot.onClick {
            if(hasPreviousPage(player))
                previousPage(this)
        }

        menu.preOpen {
            // adapting items for player
            val items = itemsAdapterOnOpen?.let { it(getPlayerItems(player).toList()) }
            if(items != null)
                currentPlayerItems[player] = items

            // set the menu page if player informed in [MenuDSL.setPlayerOpenPage]
            val openPage = menu.playerData.get(player)?.get(PAGINATION_OPEN_PAGE_KEY) as? Int?
            if(openPage != null) {
                // check if openPage is valid or cancel and trigger a event
                if(isPageAvailable(player, openPage)) {
                    currentPlayerPage[player] = openPage
                } else {
                    canceled = true
                    paginationEventHandler.pageAvailable(this)
                }
            }
        }

        // cleaning player data on menu close
        menu.onClose {
            currentPlayerPage.remove(player)
            currentPlayerItems.remove(player)

            for (value in itemPlayerSlotData.values) {
                value.remove(player)
            }
        }

        // if enabled, update next and previous slots when page change
        if(autoUpdateSwitchPageSlot) {
            onPageChange {
                menu.updateSlot(nextPageSlot, player)
                menu.updateSlot(previousPageSlot, player)
            }
        }

        // setup pagination slots
        for(line in startLine..endLine) {
            for(slotPos in startSlot..endSlot) {
                val menuSlot = calculateSlot(line, slotPos)
                val slotRoot = menu.slot(menuSlot, null)

                val paginationSlot = PaginationSlotDSLImpl(
                        this,
                        slotRoot
                )

                slotRoot.apply {
                    fun getCurrentItemForPlayer(player: Player): T? {
                        return getCurrentItemForPlayer(
                                menuSlot,
                                getPlayerCurrentPage(player),
                                player
                        )
                    }

                    onRender {
                        paginationSlot.paginationEventHandler.handleRender(
                                getCurrentItemForPlayer(player),
                                this
                        )
                    }
                    onUpdate {
                        paginationSlot.paginationEventHandler.handleUpdate(
                                getCurrentItemForPlayer(player),
                                this
                        )
                    }
                    onClick {
                        paginationSlot.paginationEventHandler.handleInteract(
                                getCurrentItemForPlayer(player),
                                this
                        )
                    }
                }

                paginationSlots.put(menuSlot, paginationSlot)
            }
        }
    }

    override fun hasPreviousPage(player: Player): Boolean {
        return isPageAvailable(player, getPlayerCurrentPage(player) -1)
    }
    override fun hasNextPage(player: Player): Boolean {
        return isPageAvailable(player, getPlayerCurrentPage(player) +1)
    }

    override fun getPlayerCurrentPage(player: Player): Int = currentPlayerPage.getOrDefault(player, 1)

    /**
     * Update current item list calling itemsUpdateFilter
     */
    override fun updateItemsToPlayer(menuPlayer: MenuPlayerInventory) {
        val player = menuPlayer.player

        // checking if adapter is not null, other wise, ignore
        if(itemsAdapterOnUpdate != null) {
            val items = itemsAdapterOnUpdate.let { menuPlayer.it(itemsProvider().toList()) }

            currentPlayerItems[player] = items

            // setting player to the last page if he is not in a page with items after adapting
            if(!isPageAvailable(player, getPlayerCurrentPage(player)))
                currentPlayerPage[player] = maxPages(player)

            // calling update slot to the PaginationSlotDSL
            forEachSlot { slotPos, pageSlot ->
                val currentPage = getPlayerCurrentPage(player)

                val item: T? = getCurrentItemForPlayer(slotPos, currentPage, player)

                pageSlot.updateSlot(item, item, slotPos, menuPlayer)
            }

            // calling event because this remove items causing no more page needed
            paginationEventHandler.pageChange(menuPlayer)
        }
    }

    internal fun getCurrentItemForPlayer(slotPos: Int, page: Int, player: Player): T? {
        val items = getPlayerItems(player)

        val startLineNotUsage = startLine - 1
        val lineEndSlotNotUsage = 9 - endSlot
        val startSlotNotUsage = startSlot -1

        val startLineSlotsUsage = startLineNotUsage * 9
        val currentLine = (slotPos / 9) +1
        val currentLineUsage = currentLine - startLineNotUsage

        val currentUsageStartSlots = startSlotNotUsage * currentLineUsage
        val currentEndSlotUsage = if(currentLineUsage > 1) lineEndSlotNotUsage * (currentLineUsage-1) else 0

        val slotItemIndex = pageStartIndex(page) + (slotPos - startLineSlotsUsage - currentUsageStartSlots - currentEndSlotUsage)

        return items.asSequence().elementAtOrNull(slotItemIndex-1)
    }

    internal fun nextPage(menuPlayerInventory: MenuPlayerInventory) {
        val nextPage = getPlayerCurrentPage(menuPlayerInventory.player) +1

        changePage(menuPlayerInventory, nextPage)
    }

    internal fun previousPage(menuPlayerInventory: MenuPlayerInventory) {
        val previousPage = getPlayerCurrentPage(menuPlayerInventory.player) -1

        changePage(menuPlayerInventory, previousPage)
    }

    private fun changePage(menuPlayerInventory: MenuPlayerInventory, nextPage: Int) {
        val player = menuPlayerInventory.player

        val currentPage = getPlayerCurrentPage(player)

        currentPlayerPage[player] = nextPage

        forEachSlot { slotPos, pageSlot ->
            val actualItem: T? = getCurrentItemForPlayer(slotPos, currentPage, player)

            val nextItem: T? = getCurrentItemForPlayer(slotPos, nextPage, player)

            pageSlot.updateSlot(actualItem, nextItem, slotPos, menuPlayerInventory, true)

            paginationEventHandler.pageChange(menuPlayerInventory)
        }
    }

    private inline fun forEachSlot(
            block: (slotPos: Int, pageSlot: PaginationSlotDSLImpl<T>) -> Unit
    ) {
        for (line in startLine..endLine) {
            for (slot in startSlot..endSlot) {
                val slotPos = calculateSlot(line, slot)
                val pageSlot = paginationSlots[slotPos] as? PaginationSlotDSLImpl<T>
                        ?: continue

                block(slotPos, pageSlot)
            }
        }
    }

    private fun pageStartIndex(page: Int) = ((page - 1) * maxSlotPerPage())

    private fun getPlayerItems(player: Player) = currentPlayerItems[player] ?: itemsProvider()

    private fun maxSlotPerPage() = (endLine - startLine +1) * (endSlot - startSlot +1)

    private fun maxPages(player: Player): Int {
        val itemsCount = getPlayerItems(player).size

        val maxPerPage = maxSlotPerPage()

        var pages = (itemsCount / maxPerPage).toInt()
        val mod = itemsCount % maxPerPage

        if(mod > 0) pages++

        return pages
    }

    private fun isPageAvailable(player: Player, page: Int): Boolean {
        val maxPages = maxPages(player)

        return page in 1..maxPages
    }
}