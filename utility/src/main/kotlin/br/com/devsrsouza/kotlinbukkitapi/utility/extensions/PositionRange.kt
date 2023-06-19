package br.com.devsrsouza.kotlinbukkitapi.utility.extensions

import br.com.devsrsouza.kotlinbukkitapi.utility.types.BlockPos
import br.com.devsrsouza.kotlinbukkitapi.utility.types.ChunkPos
import br.com.devsrsouza.kotlinbukkitapi.utility.types.VectorComparable
import br.com.devsrsouza.kotlinbukkitapi.utility.types.asBlockPos
import br.com.devsrsouza.kotlinbukkitapi.utility.types.asBukkitBlock
import br.com.devsrsouza.kotlinbukkitapi.utility.types.asBukkitChunk
import br.com.devsrsouza.kotlinbukkitapi.utility.types.asBukkitLocation
import br.com.devsrsouza.kotlinbukkitapi.utility.types.asPos
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

// BUKKIT THINGS

public operator fun PosRange<*, BlockPos>.contains(other: Location): Boolean = contains(other.asPos())
public operator fun PosRange<*, BlockPos>.contains(other: Block): Boolean = contains(other.asPos())
public operator fun PosRange<*, ChunkPos>.contains(other: Chunk): Boolean = contains(other.asPos())

public operator fun Location.rangeTo(other: Location): PosRange<Location, BlockPos> {
    return PosRange(this.asBlockPos(), other.asBlockPos()) {
        RangeIteratorWithFactor<Location, BlockPos>(
            this,
            other,
            { it.asBukkitLocation(world) },
            { it.asBlockPos() },
        )
    }
}

public operator fun Block.rangeTo(other: Block): PosRange<Block, BlockPos> {
    return PosRange(this.asPos(), other.asPos()) {
        RangeIteratorWithFactor<Block, BlockPos>(
            this,
            other,
            { it.asBukkitBlock(world) },
            { it.asPos() },
        )
    }
}

public operator fun Chunk.rangeTo(other: Chunk): PosRange<Chunk, ChunkPos> {
    return PosRange(this.asPos(), other.asPos()) {
        RangeIteratorWithFactor<Chunk, ChunkPos>(
            this,
            other,
            { it.asBukkitChunk(world) },
            { it.asPos() },
        )
    }
}

public class PosRange<T, POS : VectorComparable<POS>>(
    public val first: POS,
    public val last: POS,
    private val buildIterator: () -> Iterator<T>,
) : ClosedRange<POS>, Iterable<T> {
    override val endInclusive: POS get() = last
    override val start: POS get() = first

    override fun contains(value: POS): Boolean {
        val firstAxis = first.axis()
        val lastAxis = last.axis()
        return value.axis().withIndex().all { (index, it) ->
            it >= firstAxis[index] && it <= lastAxis[index]
        }
    }

    override fun iterator(): Iterator<T> = buildIterator()
}

public class PosRangeIterator<T : VectorComparable<T>>(
    first: T,
    last: T,
    private val factor: (axis: IntArray) -> T,
) : Iterator<T> {
    private val firstAxis = first.axis()
    private val lastAxis = last.axis()
    private val closedAxisRanges = firstAxis.mapIndexed { index, it ->
        IntProgression.fromClosedRange(it.toInt(), lastAxis[index].toInt(), 1)
    }
    private val iteratorAxis = closedAxisRanges.map { it.iterator() }.toTypedArray()

    private val actualAxis = iteratorAxis.toList().subList(0, iteratorAxis.size - 1)
        .map { it.nextInt() }
        .toTypedArray()

    override fun hasNext(): Boolean {
        return iteratorAxis.any { it.hasNext() }
    }

    override fun next(): T {
        val lastIndex = iteratorAxis.size - 1
        val last = iteratorAxis[lastIndex]
        if (last.hasNext()) {
            val axis = IntArray(actualAxis.size) { actualAxis[it] } + last.nextInt()
            return factor(axis)
        }
        for (i in lastIndex - 1 downTo 0) {
            val axis = iteratorAxis[i]
            if (axis.hasNext()) {
                actualAxis[i] = axis.nextInt()
                iteratorAxis[i + 1] = closedAxisRanges[i + 1].iterator()
                break
            }
        }
        return next()
    }
}

public class RangeIteratorWithFactor<T, POS : VectorComparable<POS>>(
    start: T,
    end: T,
    private val factor: (POS) -> T,
    private val posFactor: (T) -> POS,
) : Iterator<T> {
    public val iterator: PosRangeIterator<POS> = PosRangeIterator(posFactor(start), posFactor(end), posFactor(start)::factor)

    override fun hasNext(): Boolean = iterator.hasNext()
    override fun next(): T = factor(iterator.next())
}
