package br.com.devsrsouza.kotlinbukkitapi.utils.time

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

public fun now(): Long = System.currentTimeMillis()
public fun nowNano(): Long = System.nanoTime()

public val Long.ticks: Duration get() = toDouble().ticks
public val Int.ticks: Duration get() = toDouble().ticks
public val Double.ticks: Duration get() = tickToMilliseconds(this).milliseconds

public val Duration.inTicks: Double get() = millisecondsToTick(toDouble(DurationUnit.MILLISECONDS))

public fun Duration.toLongTicks(): Long = inTicks.toLong()

private fun tickToMilliseconds(value: Double): Double = value * 50.0
private fun millisecondsToTick(value: Double): Double = value / 50.0
