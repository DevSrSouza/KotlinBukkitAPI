package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor

import kotlinx.serialization.descriptors.SerialDescriptor

internal interface SerializationEncodeInterceptor {
    fun encodeBoolean(descriptor: SerialDescriptor, index: Int, value: Boolean): Boolean

    fun encodeByte(descriptor: SerialDescriptor, index: Int, value: Byte): Byte

    fun encodeChar(descriptor: SerialDescriptor, index: Int, value: Char): Char

    fun encodeDouble(descriptor: SerialDescriptor, index: Int, value: Double): Double

    fun encodeFloat(descriptor: SerialDescriptor, index: Int, value: Float): Float

    fun encodeInt(descriptor: SerialDescriptor, index: Int, value: Int): Int

    fun encodeLong(descriptor: SerialDescriptor, index: Int, value: Long): Long

    fun encodeShort(descriptor: SerialDescriptor, index: Int, value: Short): Short

    fun encodeString(descriptor: SerialDescriptor, index: Int, value: String): String
}
