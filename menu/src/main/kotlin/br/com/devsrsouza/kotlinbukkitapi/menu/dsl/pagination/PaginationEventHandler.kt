package br.com.devsrsouza.kotlinbukkitapi.menu.dsl.pagination

import br.com.devsrsouza.kotlinbukkitapi.menu.MenuPlayer

public typealias MenuPlayerPageChangeEvent = MenuPlayer.() -> Unit
public typealias MenuPlayerPageAvailableEvent = MenuPlayer.() -> Unit

public class PaginationEventHandler {
    internal val pageChangeCallbacks: MutableList<MenuPlayerPageChangeEvent> = mutableListOf()
    internal val pageAvailableCallbacks: MutableList<MenuPlayerPageAvailableEvent> = mutableListOf()

    public fun pageChange(pageChange: MenuPlayer) {
        for (callback in pageChangeCallbacks) {
            callback(pageChange)
        }
    }

    public fun pageAvailable(pageAvailable: MenuPlayer) {
        for (callback in pageAvailableCallbacks) {
            callback(pageAvailable)
        }
    }
}