package br.com.devsrsouza.kotlinbukkitapi.utils.time

import kotlin.time.*

fun now(): Long = System.currentTimeMillis()
fun nowNano(): Long = System.nanoTime()

val Long.ticks: Duration get() = toDouble().ticks
val Int.ticks: Duration get() = toDouble().ticks
val Double.ticks: Duration get() = Duration.milliseconds(tickToMilliseconds(this))

val Duration.inTicks: Double get() = millisecondsToTick(toDouble(DurationUnit.MILLISECONDS))

fun Duration.toLongTicks(): Long = inTicks.toLong()

private fun tickToMilliseconds(value: Double): Double = value * 50.0
private fun millisecondsToTick(value: Double): Double = value / 50.0