package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.utils.time.*
import org.junit.Assert.*
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class TimeUtilsTest {

    @OptIn(ExperimentalTime::class)
    @Test
    fun `should convert correctly seconds to ticks`() {
        val time = 1
        val tick = 20L

        assertEquals(tick, time.seconds.toLongTicks())
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `should convert correctly tick to seconds`() {
        val n = 60L
        val time = 3L

        assertEquals(time, n.ticks.toLong(DurationUnit.SECONDS))
    }
}