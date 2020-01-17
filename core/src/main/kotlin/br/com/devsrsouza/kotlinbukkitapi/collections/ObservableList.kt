package br.com.devsrsouza.kotlinbukkitapi.collections

typealias ObservableListener = (ObservableListAction) -> Unit
enum class ObservableListAction { ADD, SET, REMOVE, CLEAR }

class ObservableList<T>(
        private val list: MutableList<T>
) : MutableList<T> by list {

    private val listeners = mutableListOf<ObservableListener>()

    fun observe(listener: ObservableListener) {
        listeners.add(listener)
    }

    private fun runListeners(action: ObservableListAction) {
        for (listener in listeners) {
            listener(action)
        }
    }

    override fun add(element: T): Boolean {
        runListeners(ObservableListAction.ADD)
        return list.add(element)
    }

    override fun add(index: Int, element: T) {
        runListeners(ObservableListAction.ADD)
        return list.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        runListeners(ObservableListAction.ADD)
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        runListeners(ObservableListAction.ADD)
        return list.addAll(elements)
    }

    override fun clear() {
        list.clear()
        runListeners(ObservableListAction.CLEAR)
    }

    override fun listIterator(): MutableListIterator<T> {
        return ObservableMutableListIterator(
                list.listIterator(),
                ::runListeners
        )
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return ObservableMutableListIterator(
                list.listIterator(index),
                ::runListeners
        )
    }

    inline fun Boolean.ifTrue(block: () -> Unit) = apply { block() }

    override fun remove(element: T): Boolean {
        return list.remove(element).ifTrue {
            runListeners(ObservableListAction.REMOVE)
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return list.removeAll(elements).ifTrue {
            runListeners(ObservableListAction.REMOVE)
        }
    }

    override fun removeAt(index: Int): T {
        return list.removeAt(index).apply {
            runListeners(ObservableListAction.REMOVE)
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return list.retainAll(elements).ifTrue {
            runListeners(ObservableListAction.REMOVE)
        }
    }

    override fun set(index: Int, element: T): T {
        return list.set(index, element).apply {
            runListeners(ObservableListAction.SET)
        }
    }
}

class ObservableMutableListIterator<T>(
        private val iterator: MutableListIterator<T>,
        private val runListeners: ObservableListener
) : MutableListIterator<T> by iterator {
    override fun add(element: T) {
        iterator.add(element)
        runListeners(ObservableListAction.ADD)
    }

    override fun remove() {
        iterator.remove()
        runListeners(ObservableListAction.REMOVE)
    }

    override fun set(element: T) {
        iterator.set(element)
        runListeners(ObservableListAction.SET)
    }
}