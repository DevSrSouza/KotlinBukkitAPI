package br.com.devsrsouza.kotlinbukkitapi.utility.extensions

import kotlin.random.Random

public fun <E> Collection<E>.randomOrNull(): E? = runCatching { random() }.getOrNull()
public fun <E> Collection<E>.randomIndex(): Int = if(size > 0) Random.nextInt(size) else -1

public fun <E> Collection<E>.randomize(): MutableList<E> {
    val oldList = toMutableList()
    val newList = mutableListOf<E>()

    for(i in 0 until size) {
        val index = oldList.randomIndex()
        newList.add(oldList.get(index))
        oldList.removeAt(index)
    }

    return newList
}