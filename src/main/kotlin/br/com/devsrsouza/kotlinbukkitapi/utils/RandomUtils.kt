package br.com.devsrsouza.kotlinbukkitapi.utils

import java.util.*

private val rand by lazy { Random() }

fun ClosedRange<Int>.random() = rand.nextInt(endInclusive - start) +  start
fun <E> List<E>.random(): E? = if (size > 0) get(rand.nextInt(size)) else null