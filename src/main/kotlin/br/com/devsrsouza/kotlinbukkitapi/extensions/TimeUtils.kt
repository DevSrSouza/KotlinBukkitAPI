package br.com.devsrsouza.kotlinbukkitapi.extensions.time

val Number.millisecond get() = Millisecond(this)
val Number.tick get() = Tick(this)
val Number.second get() = Second(this)
val Number.minute get() = Minute(this)
val Number.hour get() = Hour(this)

abstract class ConvertTime(protected val number: Number) {
    abstract fun toMillisecond() : Long
    abstract fun toTick() : Long
    abstract fun toSecond() : Int
    abstract fun toMinute() : Int
    abstract fun toHour() : Int
}

class Millisecond(number: Number) : ConvertTime(number) {
    override fun toMillisecond() = number.toLong()
    override fun toTick() = toMillisecond()/50
    override fun toSecond(): Int = (toTick()/20).toInt()
    override fun toMinute(): Int = toSecond()/60
    override fun toHour(): Int = toMinute()/60
}

class Tick(number: Number) : ConvertTime(number) {
    override fun toMillisecond() = (number.toLong()*50)
    override fun toTick() = number.toLong()
    override fun toSecond(): Int = (number.toLong()/20).toInt()
    override fun toMinute(): Int = toSecond()/60
    override fun toHour(): Int = toMinute()/60
}

class Second(number: Number) : ConvertTime(number) {
    override fun toMillisecond() = toTick()*50
    override fun toTick() = number.toLong()*20
    override fun toSecond(): Int = number.toInt()
    override fun toMinute(): Int = number.toInt()/60
    override fun toHour(): Int = toMinute()/60
}

class Minute(number: Number) : ConvertTime(number) {
    override fun toMillisecond() = toTick()*50
    override fun toTick() = toSecond().toLong()*20
    override fun toSecond(): Int = number.toInt()*60
    override fun toMinute(): Int = number.toInt()
    override fun toHour(): Int = number.toInt()/60
}

class Hour(number: Number) : ConvertTime(number) {
    override fun toMillisecond() = toTick()*50
    override fun toTick() = toSecond().toLong()*20
    override fun toSecond(): Int = toMinute()*60
    override fun toMinute(): Int = number.toInt()*60
    override fun toHour(): Int = number.toInt()
}