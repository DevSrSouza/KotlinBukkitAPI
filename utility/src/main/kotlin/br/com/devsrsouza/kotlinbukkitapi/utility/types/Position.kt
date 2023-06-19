package br.com.devsrsouza.kotlinbukkitapi.utility.types

import br.com.devsrsouza.kotlinbukkitapi.utility.extensions.PosRange
import br.com.devsrsouza.kotlinbukkitapi.utility.extensions.PosRangeIterator
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import kotlin.math.sqrt

public fun blockPosOf(x: Int, y: Int, z: Int): BlockPos = BlockPos(x, y, z)
public fun locationPosOf(x: Double, y: Double, z: Double, yaw: Float = 0f, pitch: Float = 0f): LocationPos = LocationPos(x, y, z, yaw, pitch)
public fun chunkPosOf(x: Int, z: Int): ChunkPos = ChunkPos(x, z)

public fun Location.asPos(): LocationPos = LocationPos(x, y, z, yaw, pitch)
public fun LocationPos.asBukkitBlock(world: World): Block = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())
public fun LocationPos.asBukkitLocation(world: World): Location = Location(world, x, y, z)
public fun LocationPos.asBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())

public fun Block.asPos(): BlockPos = BlockPos(x, y, z)
public fun Location.asBlockPos(): BlockPos = BlockPos(blockX, blockY, blockZ)
public fun BlockPos.asBukkitBlock(world: World): Block = world.getBlockAt(x, y, z)
public fun BlockPos.asBukkitLocation(world: World?): Location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
public fun BlockPos.asLocationPos(): LocationPos = LocationPos(x.toDouble(), y.toDouble(), z.toDouble(), 0f, 0f)
public fun BlockPos.asChunkPos(): ChunkPos = ChunkPos(x shr 4, z shr 4)

public fun Chunk.asPos(): ChunkPos = ChunkPos(x, z)
public fun ChunkPos.asBukkitChunk(world: World): Chunk = world.getChunkAt(x, z)

public data class BlockPos(
        var x: Int,
        var y: Int,
        var z: Int
) : VectorComparable<BlockPos> {
    override fun axis(): DoubleArray = doubleArrayOf(x.toDouble(), y.toDouble(), z.toDouble())
    override fun factor(axis: IntArray): BlockPos = BlockPos(axis[0], axis[1], axis[2])
}

public data class LocationPos(
        var x: Double,
        var y: Double,
        var z: Double,
        val yaw: Float,
        val pitch: Float
) : VectorComparable<LocationPos> {
    override fun axis(): DoubleArray = doubleArrayOf(x, y, z)
    override fun factor(axis: IntArray): LocationPos = LocationPos(axis[0].toDouble(), axis[1].toDouble(), axis[2].toDouble(), yaw, pitch)
}

public data class ChunkPos(
        var x: Int,
        var z: Int
) : VectorComparable<ChunkPos> {
    override fun axis(): DoubleArray = doubleArrayOf(x.toDouble(), z.toDouble())
    override fun factor(axis: IntArray): ChunkPos = ChunkPos(axis[0], axis[1])
}

public interface VectorComparable<T : VectorComparable<T>> : Comparable<T> {
    public fun axis(): DoubleArray
    public fun factor(axis: IntArray): T

    public operator fun rangeTo(other: T): PosRange<T, T> {
        return PosRange(this as T, other) { PosRangeIterator(this, other, ::factor) }
    }

    override fun compareTo(other: T): Int {
        val selfAxis = axis()
        val otherAxis = other.axis()
        val pairAxis = selfAxis.mapIndexed { index, axis -> axis to otherAxis[index] }
        val (d1, d2) = calculatePythagoras(*pairAxis.toTypedArray())
        return d1.compareTo(d2)
    }
}

private fun Pair<Int, Int>.toDouble() = first.toDouble() to second.toDouble()
private fun Pair<Double, Double>.toInt() = first.toInt() to second.toInt()
private fun calculatePythagoras(vararg positions: Pair<Double, Double>): Pair<Double, Double> {
    val pow = positions.map { (x1, x2) -> (x1 * x1) to (x2 * x2) }

    val x1Sum = pow.sumOf { (x, _) -> x }
    val x2Sum = pow.sumOf { (_, x) -> x }

    val d1 = sqrt(x1Sum)
    val d2 = sqrt(x2Sum)

    return d1 to d2
}