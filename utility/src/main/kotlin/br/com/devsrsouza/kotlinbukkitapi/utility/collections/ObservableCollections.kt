package br.com.devsrsouza.kotlinbukkitapi.utility.collections

public typealias ObservableListener = (ObservableAction) -> Unit
public enum class ObservableAction { ADD, SET, REPLACE, REMOVE, CLEAR }

public fun <T> observableListOf(mutableList: MutableList<T>): ObservableList<T> = mutableList.asObservable()
public fun <T> observableSetOf(mutableSet: MutableSet<T>): ObservableSet<T> = mutableSet.asObservable()
public fun <K, V> observableMapOf(mutableMap: MutableMap<K, V>): ObservableMap<K, V> = mutableMap.asObservable()

public fun <T> MutableList<T>.asObservable(): ObservableList<T> = ObservableList(this)
public fun <T> MutableSet<T>.asObservable(): ObservableSet<T> = ObservableSet(this)
public fun <K, V> MutableMap<K, V>.asObservable(): ObservableMap<K, V> = ObservableMap(this)

public class ObservableList<T>(
    private val list: MutableList<T>,
) : MutableList<T> by list, ObservableCollection<T> {

    override val listeners: MutableList<ObservableListener> = mutableListOf<ObservableListener>()

    override fun add(element: T): Boolean {
        runListeners(ObservableAction.ADD)
        return list.add(element)
    }

    override fun add(index: Int, element: T) {
        runListeners(ObservableAction.ADD)
        return list.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        runListeners(ObservableAction.ADD)
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        runListeners(ObservableAction.ADD)
        return list.addAll(elements)
    }

    override fun clear() {
        list.clear()
        runListeners(ObservableAction.CLEAR)
    }

    override fun listIterator(): MutableListIterator<T> {
        return ObservableMutableListIterator(
            list.listIterator(),
            ::runListeners,
        )
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return ObservableMutableListIterator(
            list.listIterator(index),
            ::runListeners,
        )
    }

    override fun remove(element: T): Boolean {
        return list.remove(element).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return list.removeAll(elements).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun removeAt(index: Int): T {
        return list.removeAt(index).apply {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return list.retainAll(elements).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun set(index: Int, element: T): T {
        return list.set(index, element).apply {
            runListeners(ObservableAction.SET)
        }
    }
}

public class ObservableMutableListIterator<T>(
    private val iterator: MutableListIterator<T>,
    private val runListeners: ObservableListener,
) : MutableListIterator<T> by iterator {
    override fun add(element: T) {
        iterator.add(element)
        runListeners(ObservableAction.ADD)
    }

    override fun remove() {
        iterator.remove()
        runListeners(ObservableAction.REMOVE)
    }

    override fun set(element: T) {
        iterator.set(element)
        runListeners(ObservableAction.SET)
    }
}

public class ObservableSet<T>(
    private val set: MutableSet<T>,
) : MutableSet<T> by set, ObservableCollection<T> {

    override val listeners: MutableList<ObservableListener> = mutableListOf<ObservableListener>()

    override fun add(element: T): Boolean {
        return set.add(element).ifTrue {
            runListeners(ObservableAction.ADD)
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return set.addAll(elements).ifTrue {
            runListeners(ObservableAction.ADD)
        }
    }

    override fun clear() {
        set.clear()
        runListeners(ObservableAction.CLEAR)
    }

    override fun iterator(): MutableIterator<T> {
        return ObservableMutableIterator(
            set.iterator(),
            ::runListeners,
        )
    }

    override fun remove(element: T): Boolean {
        return set.remove(element).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return set.removeAll(elements).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return set.retainAll(elements).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }
}

public class ObservableMutableIterator<T>(
    private val iterator: MutableIterator<T>,
    private val runListeners: ObservableListener,
) : MutableIterator<T> by iterator {
    override fun remove() {
        iterator.remove()
        runListeners(ObservableAction.REMOVE)
    }
}

public class ObservableMap<K, V>(
    private val map: MutableMap<K, V>,
) : MutableMap<K, V> by map, ObservableHolder {

    override val listeners: MutableList<ObservableListener> = mutableListOf<ObservableListener>()

    override fun clear() {
        map.clear()
        runListeners(ObservableAction.CLEAR)
    }

    override fun put(key: K, value: V): V? {
        return put(key, value).apply {
            runListeners(ObservableAction.ADD)
        }
    }

    override fun putAll(from: Map<out K, V>) {
        putAll(from)
        runListeners(ObservableAction.ADD)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return map.putIfAbsent(key, value).also {
            if (it == null) runListeners(ObservableAction.ADD)
        }
    }

    override fun remove(key: K): V? {
        return map.remove(key)?.also {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun remove(key: K, value: V): Boolean {
        return map.remove(key, value).ifTrue {
            runListeners(ObservableAction.REMOVE)
        }
    }

    override fun replace(key: K, first: V, second: V): Boolean {
        return map.replace(key, first, second).ifTrue {
            runListeners(ObservableAction.REPLACE)
        }
    }

    override fun replace(key: K, value: V): V? {
        return map.replace(key, value)?.also {
            runListeners(ObservableAction.REPLACE)
        }
    }
}

public interface ObservableCollection<T> : MutableCollection<T>, ObservableHolder

public interface ObservableHolder {
    public val listeners: MutableList<ObservableListener>

    public fun observe(listener: ObservableListener) {
        listeners.add(listener)
    }

    public fun runListeners(action: ObservableAction) {
        for (listener in listeners) {
            listener(action)
        }
    }
}

private inline fun Boolean.ifTrue(block: () -> Unit) = apply { block() }
