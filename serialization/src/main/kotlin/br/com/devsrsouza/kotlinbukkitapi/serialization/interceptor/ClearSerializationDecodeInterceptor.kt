package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor

import kotlinx.serialization.descriptors.SerialDescriptor

class ClearSerializationDecodeInterceptor : SerializationDecodeInterceptor {
    override fun decodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean = value

    override fun decodeByte(descriptor: SerialDescriptor, index: Int, value: Byte): Byte = value

    override fun decodeChar(descriptor: SerialDescriptor, index: Int, value: Char): Char = value

    override fun decodeDouble(descriptor: SerialDescriptor, index: Int, value: Double): Double = value

    override fun decodeFloat(descriptor: SerialDescriptor, index: Int, value: Float): Float = value

    override fun decodeInt(descriptor: SerialDescriptor, index: Int, value: Int): Int = value

    override fun decodeLong(descriptor: SerialDescriptor, index: Int, value: Long): Long = value

    override fun decodeShort(descriptor: SerialDescriptor, index: Int, value: Short): Short = value

    override fun decodeString(descriptor: SerialDescriptor, index: Int, value: String): String = value
}