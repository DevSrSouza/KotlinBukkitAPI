package br.com.devsrsouza.kotlinbukkitapi.utils

import org.bukkit.Location

data class SimpleLocation(val x: Int, val y: Int, val z: Int) : Comparable<SimpleLocation> {
    override fun compareTo(other: SimpleLocation): Int {
        val cx = x.compareTo(other.x)
        if(cx == 0) {
            val cz = z.compareTo(other.z)
            if(cz == 0) {
                return y.compareTo(other.y)
            } else return cz
        } else return cx
    }
}

class SimpleLocationRange(val first: SimpleLocation, val last: SimpleLocation) : ClosedRange<SimpleLocation>, Iterator<SimpleLocation> {
    override val endInclusive: SimpleLocation get() = last
    override val start: SimpleLocation get() = first

    private val closedRangeX = IntProgression.fromClosedRange(first.x, last.x, 1)
    private val closedRangeY = IntProgression.fromClosedRange(first.y, last.y, 1)
    private val closedRangeZ = IntProgression.fromClosedRange(first.z, last.z, 1)

    private val iteratorX = closedRangeX.iterator()
    private var iteratorY = iteratorY()
    private var iteratorZ = iteratorZ()

    private var actualX = iteratorX.nextInt()
    private var actualY = iteratorY.nextInt()

    private inline fun iteratorY() = closedRangeY.iterator()
    private inline fun iteratorZ() = closedRangeZ.iterator()

    override fun hasNext(): Boolean {
        if (iteratorX.hasNext()) return true
        else if (iteratorY.hasNext()) return true
        else if (iteratorZ.hasNext()) return true
        return false
    }

    override fun next(): SimpleLocation {
        if(iteratorZ.hasNext()) {
            return SimpleLocation(actualX, actualY, iteratorZ.nextInt())
        } else {
            if(iteratorY.hasNext()) {
                actualY = iteratorY.nextInt()
                iteratorZ = iteratorZ()
            } else {
                if(iteratorX.hasNext()) {
                    actualX = iteratorX.nextInt()
                    iteratorY = iteratorY()
                }
            }
            return next()
        }
    }
}

operator fun SimpleLocation.rangeTo(other: SimpleLocation): SimpleLocationRange {
    return SimpleLocationRange(this, other)
}

// BUKKIT THINGS

inline fun Location.asSimple() = SimpleLocation(blockX, blockY, blockZ)

operator fun Location.rangeTo(other: Location): SimpleLocationRange {
    return asSimple()..other.asSimple()
}