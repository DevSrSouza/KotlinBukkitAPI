package br.com.devsrsouza.kotlinbukkitapi.serialization

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.BlockSerializer
import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.ChunkSerializer
import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.LocationSerializer
import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.MaterialDataSerializer
import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.MaterialSerializer
import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.WorldSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

/**
 * All KotlinBukkitAPI already built Serializers for Kotlinx.serialization.
 *
 * Serializers for: Block, Chunk, Location, MaterialData, Material, World.
 */
public fun BukkitSerialModule(): SerializersModule = SerializersModule {
    contextual(BlockSerializer)
    contextual(ChunkSerializer)
    contextual(LocationSerializer)
    contextual(MaterialDataSerializer)
    contextual(MaterialSerializer)
    contextual(WorldSerializer)
}
