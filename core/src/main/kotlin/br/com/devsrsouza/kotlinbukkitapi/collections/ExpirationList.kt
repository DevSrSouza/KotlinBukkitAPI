package br.com.devsrsouza.kotlinbukkitapi.collections

import br.com.devsrsouza.kotlinbukkitapi.extensions.plugin.WithPlugin
import br.com.devsrsouza.kotlinbukkitapi.extensions.scheduler.scheduler
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

typealias OnExpireCallback<T> = (T) -> Unit

fun <E> Plugin.expirationListOf(): ExpirationList<E> = ExpirationListImpl(this)

fun <E> WithPlugin<*>.expirationListOf() = plugin.expirationListOf<E>()

fun <E> expirationListOf(expireTime: Int, plugin: Plugin, vararg elements: E)
        = plugin.expirationListOf<E>().apply { elements.forEach { add(it, expireTime) } }

fun <E> Plugin.expirationListOf(expireTime: Int, vararg elements: E)
        = expirationListOf(expireTime, this, elements = *elements)

fun <E> WithPlugin<*>.expirationListOf(expireTime: Int, vararg elements: E)
        = plugin.expirationListOf(expireTime, *elements)

fun <E> expirationListOf(expireTime: Int, plugin: Plugin, vararg elements: Pair<E, OnExpireCallback<E>>)
        = plugin.expirationListOf<E>().apply { elements.forEach { (element, onExpire) -> add(element, expireTime, onExpire) } }

fun <E> Plugin.expirationListOf(expireTime: Int, vararg elements: Pair<E, OnExpireCallback<E>>)
        = expirationListOf(expireTime, this, elements = *elements)

fun <E> WithPlugin<*>.expirationListOf(expireTime: Int, vararg elements: Pair<E, OnExpireCallback<E>>)
        = plugin.expirationListOf(expireTime, *elements)

interface ExpirationList<E> : MutableIterable<E> {

    /**
     * Returns the count of elements in this list.
     */
    val size: Int

    /**
     * Returns `true` if the list contains no elements, `false` otherwise.
     */
    fun isEmpty(): Boolean

    /**
     * Returns the time missing to expire or `null` if the list don't contain the element
     */
    fun missingTime(element: E): Int?

    /**
     * Checks if the specified element is contained in this list.
     */
    operator fun contains(element: E): Boolean

    /**
     * Returns the element at the specified index in the list.
     */
    operator fun get(index: Int): E?

    /**
     * Returns the index of the first occurrence of the e element in the list, or -1 if the specified
     * element is not contained in the list.
     */
    fun indexOf(element: E): Int

    /**
     * Return the first element in the list or `null` if the list is empty
     */
    fun first(): E?

    /**
     * Return the last element in the list or `null` if the list is empty
     */
    fun last(): E?

    /**
     * Removes all elements from this list.
     */
    fun clear()

    /**
     * Add the element to the list with an expiration time.
     *
     * [expireTime] in seconds
     * callback [onExpire] is called when the element expires.
     */
    fun add(element: E, expireTime: Int, onExpire: OnExpireCallback<E>? = null)

    /**
     * Add the element in the start of list with an expiration time.
     *
     * [expireTime] in seconds
     * callback [onExpire] is called when the element expires.
     */
    fun addFirst(element: E, expireTime: Int, onExpire: OnExpireCallback<E>? = null)

    /**
     * Removes an element at the specified [index] from the list.
     *
     * @return the element that has been removed or `null` if
     * the element is not contained in the list.
     */
    fun removeAt(index: Int): E?

    /**
     * Removes an element from the list
     *
     * @return `true` if the element was removed.
     */
    fun remove(element: E): Boolean

    /**
     * Removes the first element from the list.
     *
     * @return the element if was removed.
     */
    fun removeFirst(): E?

    /**
     * Removes the last element from the list.
     *
     * @return the element if was removed.
     */
    fun removeLast(): E?

}

private class ExpirationNode<E>(var element: E, val expireTime: Int) {

    var next: ExpirationNode<E>? = null
    var previous: ExpirationNode<E>? = null

    var onExpire: OnExpireCallback<E>? = null
    val startTime: Long = System.currentTimeMillis()
}

class ExpirationListImpl<E>(private val plugin: Plugin) : ExpirationList<E> {

    private var head: ExpirationNode<E>? = null
    private var tail: ExpirationNode<E>? = null

    private var task: BukkitTask? = null
    private var emptyCount: Byte = 0

    override val size get() = _size
    var _size: Int = 0
        private set

    override fun isEmpty(): Boolean = _size == 0

    override fun missingTime(element: E): Int? {
        return getNodeByElement(element)
                ?.let { it.expireTime - ((System.currentTimeMillis() - it.startTime) / 1000) }
                ?.toInt()
    }

    override operator fun contains(element: E) = indexOf(element) > -1

    override operator fun get(index: Int): E? {
        return getNode(index)?.element
    }

    override fun first() = head?.element

    override fun last() = tail?.element

    override operator fun iterator(): MutableIterator<E> {
        return object : MutableIterator<E> {
            private val nodeIterator = nodeIterator()

            override fun hasNext() = nodeIterator.hasNext()
            override fun next() = nodeIterator.next().element
            override fun remove() = nodeIterator.remove()
        }
    }

    override fun indexOf(element: E): Int {
        if (head == null) return -1
        var node = head
        var count = 0
        do {
            if (node?.element === element) {
                return count
            }
            node = node?.next
            count++
        } while (node != null)
        return -1
    }

    override fun clear() {
        head = null
        tail = null
        _size = 0
    }

    override fun add(element: E, expireTime: Int, onExpire: OnExpireCallback<E>?) {
        if(expireTime <= 0) throw IllegalArgumentException("expireTime need to be greater then 0")
        val newNode = ExpirationNode(element, expireTime).also { it.onExpire = onExpire }
        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            tail?.next = newNode
            tail = newNode
        }
        _size++
        generateTask()
    }

    override fun addFirst(element: E, expireTime: Int, onExpire: OnExpireCallback<E>?) {
        if(expireTime <= 0) throw IllegalArgumentException("expireTime need to be greater then 0")
        val newNode = ExpirationNode(element, expireTime).also { it.onExpire = onExpire }
        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            head?.previous = newNode
            head = newNode
        }
        _size++
        generateTask()
    }

    override fun removeAt(index: Int): E? {
        return getNode(index)?.also {
            removeNode(it)
        }?.element
    }

    override fun remove(element: E) = getNodeByElement(element)?.let { true.apply { removeNode(it) } } ?: false

    override fun removeFirst(): E? {
        val next = head?.next
        val headElement = head?.element
        if(next == null) {
            tail = null
            head = null
        } else {
            head = next
            next.previous = null
        }
        return headElement
    }

    override fun removeLast(): E? {
        val previous = tail?.previous
        val tailElement = tail?.element
        if(previous == null) {
            tail = null
            head = null
        } else {
            tail = previous
            previous.next = null
        }
        return tailElement
    }

    private fun getNode(index: Int): ExpirationNode<E>? {
        if (index < 0 || index >= _size) return null

        val mid = _size / 2
        return if (index > mid)
            getFromSpecificSide(index - mid, tail) { it?.previous }
        else
            getFromSpecificSide(index, head) { it?.next }
    }

    private fun getNodeByElement(element: E): ExpirationNode<E>? {
        if (head == null) return null
        var node = head
        do {
            if (node?.element === element) {
                return node
            }
            node = node?.next
        } while (node != null)
        return null
    }

    private inline fun getFromSpecificSide(count: Int, start: ExpirationNode<E>?,
                                           next: (ExpirationNode<E>?) -> ExpirationNode<E>?): ExpirationNode<E> {
        var index = 0
        var current = start
        while (index != count) {
            current = next(current)
            index++
        }
        return current!!
    }

    private fun nodeIterator(): MutableIterator<ExpirationNode<E>> {
        return object : MutableIterator<ExpirationNode<E>> {

            private var current = head

            override fun hasNext(): Boolean {
                return current != null
            }

            override fun next(): ExpirationNode<E> {
                val aux = current
                current = current?.next
                return aux!!
            }

            override fun remove() {
                if (current != null)
                    removeNode(current!!)
            }
        }
    }

    private fun checkTime(current: Long, node: ExpirationNode<E>)
            = ((current - node.startTime) / 1000) - node.expireTime >= 0

    private fun generateTask() {
        if (task == null) {
            task = scheduler {
                if (isEmpty())
                    emptyCount++
                else {
                    emptyCount = 0
                    val current = System.currentTimeMillis()
                    for (node in nodeIterator()) {
                        if (checkTime(current, node)) {
                            if (node.onExpire != null) node.onExpire?.invoke(node.element)
                            removeNode(node)
                        }
                    }
                }
                if (emptyCount > 9) {
                    cancel()
                    task = null
                }
            }.runTaskTimer(plugin, 0L, 20L)
        }
    }

    private fun removeNode(node: ExpirationNode<E>) {
        if (node === head && node === tail) {
            head = null
            tail = null
        } else if (node === head) {
            head = node.next?.apply { previous = null }
        } else if (node === tail) {
            tail = node.previous?.apply { next = null }
        } else {
            node.apply {
                previous?.next = next
                next?.previous = previous
            }
        }
        _size--
    }
}

