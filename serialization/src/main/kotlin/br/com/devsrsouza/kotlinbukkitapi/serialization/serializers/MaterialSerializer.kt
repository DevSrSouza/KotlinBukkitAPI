package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers

import br.com.devsrsouza.kotlinbukkitapi.serialization.serializers.exceptions.MaterialNotFoundException
import kotlinx.serialization.*
import org.bukkit.Material

object MaterialSerializer : KSerializer<Material> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor("org.bukkit.Material", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Material {
        val materialName = decoder.decodeString()

        return Material.values().find { it.name.equals(materialName, true) }
                ?: throw MaterialNotFoundException(materialName)
    }

    override fun serialize(encoder: Encoder, value: Material) {
        encoder.encodeString(value.name.toLowerCase())
    }

}