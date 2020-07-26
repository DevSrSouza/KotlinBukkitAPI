package br.com.devsrsouza.kotlinbukkitapi.serialization

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

/**
 * All KotlinBukkitAPI already built Serializers for Kotlinx.serialization.
 *
 * Serializers for: Block, Chunk, Location, MaterialData, Material, World.
 */
fun BukkitSerialModule() = SerializersModule {
    contextual(BlockSerializer)
    contextual(ChunkSerializer)
    contextual(LocationSerializer)
    contextual(MaterialDataSerializer)
    contextual(MaterialSerializer)
    contextual(WorldSerializer)
}