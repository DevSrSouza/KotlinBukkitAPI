package br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.impl.decoder

import br.com.devsrsouza.kotlinbukkitapi.serialization.interceptor.SerializationDecodeInterceptor
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder

internal class CompositeDecoderInterceptor(
    val interceptor: SerializationDecodeInterceptor,
    val delegate: CompositeDecoder,
) : CompositeDecoder by delegate {

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean {
        return interceptor.decodeBoolean(descriptor, index, delegate.decodeBooleanElement(descriptor, index))
    }

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte {
        return interceptor.decodeByte(descriptor, index, delegate.decodeByteElement(descriptor, index))
    }

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char {
        return interceptor.decodeChar(descriptor, index, delegate.decodeCharElement(descriptor, index))
    }

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double {
        return interceptor.decodeDouble(descriptor, index, delegate.decodeDoubleElement(descriptor, index))
    }

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float {
        return interceptor.decodeFloat(descriptor, index, delegate.decodeFloatElement(descriptor, index))
    }

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        return interceptor.decodeInt(descriptor, index, delegate.decodeIntElement(descriptor, index))
    }

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long {
        return interceptor.decodeLong(descriptor, index, delegate.decodeLongElement(descriptor, index))
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short {
        return interceptor.decodeShort(descriptor, index, delegate.decodeShortElement(descriptor, index))
    }

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
        return interceptor.decodeString(descriptor, index, delegate.decodeStringElement(descriptor, index))
    }

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?,
    ): T {
        return delegate.decodeSerializableElement(
            descriptor,
            index,
            DeserializationStrategyInterceptor(interceptor, deserializer),
        )
    }
}
