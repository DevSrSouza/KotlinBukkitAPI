package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.exceptions.WorldNotFoundException
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Chunk

public object ChunkSerializer : KSerializer<Chunk> {
    private const val CHUNK_SEPARATOR = ";"

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("org.bukkit.Chunk", PrimitiveKind.STRING)

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