package br.com.devsrsouza.kotlinbukkitapi.utils.time

fun now(): Long = System.currentTimeMillis()
fun nowNano(): Long = System.nanoTime()

val Number.millisecond: Millisecond get() = Millisecond(this)
val Number.tick: Tick get() = Tick(this)
val Number.second: Second get() = Second(this)
val Number.minute: Minute get() = Minute(this)
val Number.hour: Hour get() = Hour(this)

interface ConvertTime {
    fun toMillisecond(): Long
    fun toTick(): Long
    fun toSecond(): Int
    fun toMinute(): Int
    fun toHour(): Int
}

inline class Millisecond(private val number: Number) : ConvertTime {
    override fun toMillisecond() = number.toLong()
    override fun toTick() = toMillisecond()/50
    override fun toSecond(): Int = (toTick()/20).toInt()
    override fun toMinute(): Int = toSecond()/60
    override fun toHour(): Int = toMinute()/60
}

inline class Tick(private val number: Number) : ConvertTime {
    override fun toMillisecond() = (number.toLong()*50)
    override fun toTick() = number.toLong()
    override fun toSecond(): Int = (number.toLong()/20).toInt()
    override fun toMinute(): Int = toSecond()/60
    override fun toHour(): Int = toMinute()/60
}

inline class Second(private val number: Number) : ConvertTime {
    override fun toMillisecond() = toTick()*50
    override fun toTick() = number.toLong()*20
    override fun toSecond(): Int = number.toInt()
    override fun toMinute(): Int = number.toInt()/60
    override fun toHour(): Int = toMinute()/60
}

inline class Minute(private val number: Number) : ConvertTime {
    override fun toMillisecond() = toTick()*50
    override fun toTick() = toSecond().toLong()*20
    override fun toSecond(): Int = number.toInt()*60
    override fun toMinute(): Int = number.toInt()
    override fun toHour(): Int = number.toInt()/60
}

inline class Hour(private val number: Number) : ConvertTime {
    override fun toMillisecond() = toTick()*50
    override fun toTick() = toSecond().toLong()*20
    override fun toSecond(): Int = toMinute()*60
    override fun toMinute(): Int = number.toInt()*60
    override fun toHour(): Int = number.toInt()
}