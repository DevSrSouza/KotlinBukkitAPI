package br.com.devsrsouza.kotlinbukkitapi.serialization

import kotlinx.serialization.*
import net.md_5.bungee.api.ChatColor

@Serializer(forClass = String::class)
object ChangeColor : KSerializer<String> {
    private val code = '&'

    override val descriptor = PrimitiveDescriptor(
        "ChangeColor", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        fun translate(str: String) = ChatColor.translateAlternateColorCodes(code, str)
        return translate(decoder.decodeString())
    }
    override fun serialize(encoder: Encoder, value: String) {
        fun translate(str: String) = str.replace(ChatColor.COLOR_CHAR, code)
        encoder.encodeString(translate(value))
    }
}