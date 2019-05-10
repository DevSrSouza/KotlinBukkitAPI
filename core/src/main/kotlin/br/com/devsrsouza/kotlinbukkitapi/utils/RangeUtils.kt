package br.com.devsrsouza.kotlinbukkitapi.utils

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import kotlin.math.sqrt

data class SimpleLocation(val x: Int, val y: Int, val z: Int) : Comparable<SimpleLocation> {
    override fun compareTo(other: SimpleLocation): Int {
        val d1 = sqrt((x * x + y * y + z * z).toDouble())
        val d2 = other.run { sqrt((x * x + y * y + z * z).toDouble()) }

        return d1.compareTo(d2)
    }
}

class SimpleLocationRange<T>(
        val first: SimpleLocation,
        val last: SimpleLocation,
        val buildIterator: () -> Iterator<T>
) : ClosedRange<SimpleLocation>, Iterable<T> {
    override val endInclusive: SimpleLocation get() = last
    override val start: SimpleLocation get() = first

    override fun contains(value: SimpleLocation): Boolean {
        return value.x >= first.x && value.x <= last.x
                && value.y >= first.y && value.y <= last.y
                && value.z >= first.z && value.z <= last.z
    }

    override fun iterator(): Iterator<T> = buildIterator()
}

class SimpleLocationRangeIterator(first: SimpleLocation, last: SimpleLocation) : Iterator<SimpleLocation> {
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
        if (iteratorZ.hasNext()) {
            return SimpleLocation(actualX, actualY, iteratorZ.nextInt())
        } else {
            if (iteratorY.hasNext()) {
                actualY = iteratorY.nextInt()
                iteratorZ = iteratorZ()
            } else {
                if (iteratorX.hasNext()) {
                    actualX = iteratorX.nextInt()
                    iteratorY = iteratorY()
                }
            }
            return next()
        }
    }
}

operator fun SimpleLocation.rangeTo(other: SimpleLocation): SimpleLocationRange<SimpleLocation> {
    return SimpleLocationRange(this, other) { SimpleLocationRangeIterator(this, other) }
}

// BUKKIT THINGS

operator fun SimpleLocationRange<*>.contains(other: Location) = contains(other.asSimple())
operator fun SimpleLocationRange<*>.contains(other: Block) = contains(other.asSimple())

inline fun Location.asSimple() = SimpleLocation(blockX, blockY, blockZ)
inline fun SimpleLocation.asBukkitLocation(world: World) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

inline fun Block.asSimple() = SimpleLocation(x, y, z)
inline fun SimpleLocation.asBukkitBlock(world: World) = world.getBlockAt(x, y, z)

class LocationRangeIterator(val start: Location, val end: Location) : Iterator<Location> {
    val iterator = SimpleLocationRangeIterator(start.asSimple(), end.asSimple())

    override fun hasNext() = iterator.hasNext()
    override fun next() = iterator.next().asBukkitLocation(start.world)
}

operator fun Location.rangeTo(other: Location): SimpleLocationRange<Location> {
    return SimpleLocationRange(this.asSimple(), other.asSimple()) { LocationRangeIterator(this, other) }
}

class BlockRangeIterator(val start: Block, val end: Block) : Iterator<Block> {
    val iterator = SimpleLocationRangeIterator(start.asSimple(), end.asSimple())

    override fun hasNext() = iterator.hasNext()
    override fun next() = iterator.next().asBukkitBlock(start.world)
}

operator fun Block.rangeTo(other: Block): SimpleLocationRange<Block> {
    return SimpleLocationRange(this.asSimple(), other.asSimple()) { BlockRangeIterator(this, other) }
}