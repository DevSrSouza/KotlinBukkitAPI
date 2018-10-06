package br.com.devsrsouza.kotlinbukkitapi.utils

import java.util.*

private val rand by lazy { Random() }

fun ClosedRange<Int>.random() = rand.nextInt(endInclusive - start) +  start
fun <E> List<E>.randomIndex(): Int = if(size > 0) rand.nextInt(size) else -1
fun <E> List<E>.random(): E = if(size > 0) get(rand.nextInt(size)) else throw IndexOutOfBoundsException()
fun <E> List<E>.randomOrNull(): E? = if(size > 0) get(rand.nextInt(size)) else null
fun <E> List<E>.randomize(): MutableList<E> {
    val oldList = toMutableList()
    val newList = mutableListOf<E>()

    for(i in 0 until size) {
        val index = oldList.randomIndex()
        newList.add(oldList.get(index))
        oldList.removeAt(index)
    }

    return newList
}