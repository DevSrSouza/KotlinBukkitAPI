package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.exceptions.WorldNotFoundException
import kotlinx.serialization.*
import org.bukkit.Bukkit
import org.bukkit.Chunk

object ChunkSerializer : KSerializer<Chunk> {
    private const val CHUNK_SEPARATOR = ";"

    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("org.bukkit.Chunk", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Chunk {
        val raw = decoder.decodeString()

        val slices = raw.split(CHUNK_SEPARATOR)
        val worldName = slices[0]
        val world = Bukkit.getWorld(worldName) ?: throw WorldNotFoundException(worldName)

        return world.getChunkAt(
                slices[1].toInt(),
                slices[2].toInt()
        )
    }

    override fun serialize(encoder: Encoder, value: Chunk) {
        encoder.encodeString("${value.world.name}$CHUNK_SEPARATOR${value.x}$CHUNK_SEPARATOR${value.z}")
    }
}