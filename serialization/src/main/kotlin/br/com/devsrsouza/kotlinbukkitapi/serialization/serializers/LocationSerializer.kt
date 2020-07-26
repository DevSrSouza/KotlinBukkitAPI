package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.exceptions.WorldNotFoundException
import kotlinx.serialization.*
import org.bukkit.Bukkit
import org.bukkit.Location

object LocationSerializer : KSerializer<Location> {
    private const val LOCATION_SEPARATOR = ";"

    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("org.bukkit.Location", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Location {
        val raw = decoder.decodeString()

        val slices = raw.split(LOCATION_SEPARATOR)
        val worldName = slices[0]
        val world = Bukkit.getWorld(worldName) ?: throw WorldNotFoundException(worldName)

        return Location(
                world,
                slices[1].toDouble(),
                slices[2].toDouble(),
                slices[3].toDouble(),
                slices.getOrNull(4)?.toFloat() ?: 0f,
                slices.getOrNull(5)?.toFloat() ?: 0f
        )
    }

    override fun serialize(encoder: Encoder, value: Location) {
        encoder.encodeString("${value.world.name}$LOCATION_SEPARATOR${value.x}$LOCATION_SEPARATOR${value.y}$LOCATION_SEPARATOR${value.z}$LOCATION_SEPARATOR${value.yaw}$LOCATION_SEPARATOR${value.pitch}")
    }
}