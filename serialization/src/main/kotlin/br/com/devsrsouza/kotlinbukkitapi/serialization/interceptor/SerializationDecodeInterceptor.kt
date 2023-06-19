package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor

import kotlinx.serialization.descriptors.SerialDescriptor

internal interface SerializationDecodeInterceptor {
    fun decodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean

    fun decodeByte(descriptor: SerialDescriptor, index: Int, value: Byte): Byte

    fun decodeChar(descriptor: SerialDescriptor, index: Int, value: Char): Char

    fun decodeDouble(descriptor: SerialDescriptor, index: Int, value: Double): Double

    fun decodeFloat(descriptor: SerialDescriptor, index: Int, value: Float): Float

    fun decodeInt(descriptor: SerialDescriptor, index: Int, value: Int): Int

    fun decodeLong(descriptor: SerialDescriptor, index: Int, value: Long): Long

    fun decodeShort(descriptor: SerialDescriptor, index: Int, value: Short): Short

    fun decodeString(descriptor: SerialDescriptor, index: Int, value: String): String
}
