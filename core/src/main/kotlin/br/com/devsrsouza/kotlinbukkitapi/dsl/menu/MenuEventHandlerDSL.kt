package br.com.devsrsouza.kotlinbukkitapi.dsl.menu

import br.com.devsrsouza.kotlinbukkitapi.menu.*

typealias MenuPlayerUpdateEvent = MenuPlayerUpdate.() -> Unit
typealias MenuPlayerCloseEvent = MenuPlayerClose.() -> Unit
typealias MenuPlayerMoveToEvent = MenuPlayerMoveTo.() -> Unit

typealias MenuPlayerPreOpenEvent = MenuPlayerPreOpen.() -> Unit
typealias MenuPlayerOpenEvent = MenuPlayerOpen.() -> Unit

class MenuEventHandlerDSL(val menu: MenuDSL) : MenuEventHandler {

    val updateCallbacks: MutableList<MenuPlayerUpdateEvent> = mutableListOf()
    val closeCallbacks: MutableList<MenuPlayerCloseEvent> = mutableListOf()
    val moveToMenuCallbacks: MutableList<MenuPlayerMoveToEvent> = mutableListOf()
    val preOpenCallbacks: MutableList<MenuPlayerPreOpenEvent> = mutableListOf()
    val openCallbacks: MutableList<MenuPlayerOpenEvent> = mutableListOf()

    override fun update(update: MenuPlayerUpdate) {
        for (callback in updateCallbacks) {
            callback(update)
        }
    }

    override fun close(close: MenuPlayerClose) {
        for (callback in closeCallbacks) {
            callback(close)
        }
    }

    override fun moveToMenu(moveToMenu: MenuPlayerMoveTo) {
        for (callback in moveToMenuCallbacks) {
            callback(moveToMenu)
        }
    }

    override fun preOpen(preOpen: MenuPlayerPreOpen) {
        for (callback in preOpenCallbacks) {
            callback(preOpen)
        }
    }

    override fun open(open: MenuPlayerOpen) {
        for (callback in openCallbacks) {
            callback(open)
        }
    }

}