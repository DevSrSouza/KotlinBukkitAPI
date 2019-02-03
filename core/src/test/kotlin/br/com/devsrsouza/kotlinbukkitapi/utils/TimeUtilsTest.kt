package br.com.devsrsouza.kotlinbukkitapi.utils

import br.com.devsrsouza.kotlinbukkitapi.utils.time.*
import org.junit.Assert.*
import org.junit.Test

class TimeUtilsTest {

    @Test
    fun `should convert correctly milliseconds to milliseconds`() {
        val time = 300L
        val millisecond = 300L

        assertEquals(millisecond, time.millisecond.toMillisecond())
    }

    @Test
    fun `should convert correctly milliseconds to ticks`() {
        val time = 250L
        val tick = 5L

        assertEquals(tick, time.millisecond.toTick())
    }

    @Test
    fun `should convert correctly milliseconds to seconds`() {
        val time = 5000L
        val second = 5

        assertEquals(second, time.millisecond.toSecond())
    }

    @Test
    fun `should convert correctly milliseconds to minutes`() {
        val time = 60_000L
        val minute = 1

        assertEquals(minute, time.millisecond.toMinute())
    }

    @Test
    fun `should convert correctly milliseconds to hours`() {
        val time = 7_200_000L
        val hour = 2

        assertEquals(hour, time.millisecond.toHour())
    }

    @Test
    fun `should convert correctly ticks to milliseconds`() {
        val time = 20L
        val millisecond = 1000L

        assertEquals(millisecond, time.tick.toMillisecond())
    }

    @Test
    fun `should convert correctly ticks to ticks`() {
        val time = 20L
        val tick = 20L

        assertEquals(tick, time.tick.toTick())
    }

    @Test
    fun `should convert correctly ticks to seconds`() {
        val time = 20L
        val second = 1

        assertEquals(second, time.tick.toSecond())
    }

    @Test
    fun `should convert correctly ticks to minutes`() {
        val time = 2_400L
        val minute = 2

        assertEquals(minute, time.tick.toMinute())
    }

    @Test
    fun `should convert correctly ticks to hours`() {
        val time = 144_000L
        val hour = 2

        assertEquals(hour, time.tick.toHour())
    }

    @Test
    fun `should convert correctly seconds to milliseconds`() {
        val time = 8
        val millisecond = 8000L

        assertEquals(millisecond, time.second.toMillisecond())
    }

    @Test
    fun `should convert correctly seconds to ticks`() {
        val time = 2
        val tick = 40L

        assertEquals(tick, time.second.toTick())
    }

    @Test
    fun `should convert correctly seconds to seconds`() {
        val time = 20
        val second = 20

        assertEquals(second, time.second.toSecond())
    }

    @Test
    fun `should convert correctly seconds to minutes`() {
        val time = 120
        val minute = 2

        assertEquals(minute, time.second.toMinute())
    }

    @Test
    fun `should convert correctly seconds to hours`() {
        val time = 7_200
        val hour = 2

        assertEquals(hour, time.second.toHour())
    }

    @Test
    fun `should convert correctly minutes to milliseconds`() {
        val time = 1
        val millisecond = 60_000L

        assertEquals(millisecond, time.minute.toMillisecond())
    }

    @Test
    fun `should convert correctly minutes to ticks`() {
        val time = 1
        val tick = 1_200L

        assertEquals(tick, time.minute.toTick())
    }

    @Test
    fun `should convert correctly minutes to seconds`() {
        val time = 1
        val second = 60

        assertEquals(second, time.minute.toSecond())
    }

    @Test
    fun `should convert correctly minutes to minutes`() {
        val time = 25
        val minute = 25

        assertEquals(minute, time.minute.toMinute())
    }

    @Test
    fun `should convert correctly minutes to hours`() {
        val time = 120
        val hour = 2

        assertEquals(hour, time.minute.toHour())
    }

    @Test
    fun `should convert correctly hours to milliseconds`() {
        val time = 1
        val millisecond = 3_600_000L

        assertEquals(millisecond, time.hour.toMillisecond())
    }

    @Test
    fun `should convert correctly hours to ticks`() {
        val time = 2
        val tick = 144_000L

        assertEquals(tick, time.hour.toTick())
    }

    @Test
    fun `should convert correctly hours to seconds`() {
        val time = 3
        val second = 10_800

        assertEquals(second, time.hour.toSecond())
    }

    @Test
    fun `should convert correctly hours to minutes`() {
        val time = 3
        val minute = 180

        assertEquals(minute, time.hour.toMinute())
    }

    @Test
    fun `should convert correctly hours to hours`() {
        val time = 3
        val hour = 3

        assertEquals(hour, time.hour.toHour())
    }
}