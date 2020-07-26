package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.exceptions.WorldNotFoundException
import kotlinx.serialization.*
import org.bukkit.Bukkit
import org.bukkit.World

object WorldSerializer : KSerializer<World> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("org.bukkit.World", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): World {
        val worldName = decoder.decodeString()

        return Bukkit.getWorld(worldName) ?: throw WorldNotFoundException(worldName)
    }

    override fun serialize(encoder: Encoder, value: World) {
        encoder.encodeString(value.name)
    }

}