package br.com.devsrsouza.kotlinbukkitapi.serialization.serializers

import br.com.devsrsouza.kotlinbukkitapi.extensions.item.asMaterialData
import kotlinx.serialization.*
import org.bukkit.Material
import org.bukkit.material.MaterialData

@Serializer(forClass = MaterialData::class)
object MaterialDataSerializer : KSerializer<MaterialData> {
    override val descriptor: SerialDescriptor = SerialDescriptor(
        "MaterialData", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): MaterialData {
        return fromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: MaterialData) {
        encoder.encodeString(toString(value))
    }

    private fun toString(material: MaterialData): String {
        return "${material.itemTypeId}:${material.data}"
    }

    private fun fromString(content: String): MaterialData {
        val slices = content.split(":")

        val material = slices[0]
        val data = slices.getOrNull(1)?.toIntOrNull() ?: 0

        return (material.toIntOrNull()?.let {
            Material.getMaterial(it)
        } ?: Material.getMaterial(material)).asMaterialData(data.toByte())
    }

}